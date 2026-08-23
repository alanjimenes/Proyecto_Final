use Clinica;


--     Vistas


/* Qué hace: Cruza las tablas medico, persona y especialidad utilizando un INNER JOIN, filtrando únicamente a los médicos que 
tienen su estado en activo (estado = 1). Muestra su cédula, nombre completo, especialidad, teléfono y el límite de citas diarias. */

/* Para qué sirve: Nos ahorra escribir un JOIN extenso cada vez que se necesite llenar combobox o una tabla con los médicos disponibles en el sistema.
Ya que cada vez que necesitemos eso, lo podemos obtener con solo un SELECT * FROM vw_directorio_medico. */

create view vw_directorio_medico as
select medico.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, persona.cedula, persona.telefono, 
persona.estado, persona.direccion, persona.genero, medico.maxcitaspordia, especialidad.codigo_especialidad, 
especialidad.nombre as nombre_esp
from medico 
inner join persona on medico.codigo_persona = persona.codigo_persona
inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad
where persona.estado = 1;


/* Qué hace: Conecta la tabla lote_vacuna con la tabla vacuna. Filtra automáticamente para mostrar solo aquellos lotes 
que tengan existencia mayor a cero (cantidad > 0) y cuya fecha de vencimiento sea igual o posterior al día actual (>= GETDATE()). */

/* Para qué sirve: Evita que el personal médico seleccione vacunas vencidas o agotadas al momento de registrar una 
vacunación a un paciente, garantizando la seguridad clínica desde la base de datos. */

create view vw_inventario_vacunas_disponibles as
select lote_vacuna.codigo_lote, vacuna.codigo_vacuna, vacuna.nombre as nombre_vacuna, lote_vacuna.no_lote, 
lote_vacuna.cantidad, lote_vacuna.fechaVencimiento
from lote_vacuna
inner join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna
where lote_vacuna.cantidad > 0 and lote_vacuna.fechaVencimiento >= getdate();



--  Procedimientos almacenados


/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Primero, inserta los datos principales de la consulta médica 
en la tabla consulta, luego, captura automáticamente el código generado (SCOPE_IDENTITY()) e inserta los signos vitales 
y datos antropométricos correspondientes en la tabla evaluacionFisica. 
Finalmente si todo sale bien, guarda los cambios (COMMIT), y si algo falla revierte todo (ROLLBACK). */

/* Para qué sirve: Garantiza la consistencia de los datos. En nuestro sistema médico, una consulta y su evaluación física van 
de la mano, este procedimiento evita que se guarde una consulta a medias si ocurre un error en el sistema a mitad del proceso. */

create procedure sp_registrar_consulta
    @codigo_medico int, @codigo_cliente int, @fechaConsulta datetime, @sintomas text, @diagnostico text,
    @temperatura float, @frecuenciaCardiaca int, @presionArterial varchar(20), @peso float, @talla float
as
begin
    begin transaction;
    begin try
        -- Insertar la consulta
        insert into consulta (codigo_medico, codigo_cliente, fechaConsulta, sintomas, diagnostico, addResumen)
        values (@codigo_medico, @codigo_cliente, @fechaConsulta, @sintomas, @diagnostico, 1);

        declare @codigo_cons int = scope_identity();

        -- Insertar la evaluación física asociada
        insert into evaluacionFisica (codigo_cons, temperatura, frecuenciaCardiaca, presionArterial, peso, talla)
        values (@codigo_cons, @temperatura, @frecuenciaCardiaca, @presionArterial, @peso, @talla);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;


/* Qué hace: Recibe una cédula como parámetro, busca el código interno de la persona, y ejecuta dos operaciones en una 
sola transacción: cambia el estado de la persona a inactivo (estado = 0) y busca todas sus citas futuras que 
estuvieran pendientes para pasarlas automáticamente a estado 'Cancelada'. */

/* Para qué sirve: Automatiza el proceso de dar de baja a un paciente o usuario. Así nos aseguramos de que un paciente inactivo 
no se quede con citas fantasma o pendientes ocupando la agenda de los médicos. */

create procedure sp_desactivar_persona
    @cedula varchar(15)
as 
begin
    declare @codigo_persona int;
    select @codigo_persona = codigo_persona 
    from persona 
    where cedula = @cedula;

    begin transaction;
    begin try
        -- Desactivar persona
        update persona set estado = 0 
        where codigo_persona = @codigo_persona;

        -- Cancelar citas futuras pendientes
        update cita 
        set estado = 'Cancelada' 
        where codigo_cliente = @codigo_persona and fechaCita >= GETDATE() and estado = 'Pendiente';

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;



/* Qué hace: Recibe una cédula como parámetro y actualiza directamente el estado de la persona a activo (estado = 1) 
en la tabla correspondiente, permitiéndole nuevamente el acceso al sistema. */

/*Para qué sirve: Automatiza el proceso de reactivar a un paciente o usuario de manera rápida y directa, habilitando su perfil 
para que pueda volver a utilizar los servicios clínicos y agendar citas. */

create procedure sp_activar_persona
    @cedula varchar(20) as begin
    update persona 
    set estado = 1 
    where cedula = @cedula;
end;




--  Disparadores(Triggers)


/* Qué hace: Es un disparador de tipo AFTER INSERT en la tabla regVacuna. Cada vez que se registra la aplicación de una vacuna a 
un paciente, el trigger detecta qué lote se usó y le resta exactamente 1 a la cantidad disponible en la tabla lote_vacuna. */

/* Para qué sirve: Automatiza el control del inventario. El personal de enfermería solo registra la vacunación y el sistema 
se encarga de descontar la dosis del almacén en tiempo real, evitando errores humanos de conteo manual. */

create trigger trg_descontar_stock_vacuna
on regVacuna
after insert
as
begin
    -- Descuenta 1 unidad del lote correspondiente por cada registro insertado
    update lote_vacuna
    set cantidad = cantidad - 1
    from lote_vacuna 
    inner join inserted on lote_vacuna.codigo_lote = inserted.codigo_lote;
end


/* Qué hace: Es un trigger que antes de permitir que se guarde un nuevo lote o se modifique uno existente, revisa si la 
fecha de vencimiento es menor a la fecha actual (< GETDATE()).  Si detecta que está vencido, lanza un error con RAISERROR 
y cancela la operación con un ROLLBACK. */

/* Para qué sirve: Funciona como un mecanismo de seguridad a nivel de base de datos que imposibilita físicamente que 
alguien ingrese un medicamento o vacuna caducada por descuido al sistema. */

create trigger trg_validar_vencimiento_lote
on lote_vacuna
instead of insert, update
as
begin
    if exists (select 1 from inserted where fechaVencimiento < GETDATE())
    begin
        raiserror('Error: No se puede registrar o actualizar un lote con una fecha de vencimiento pasada.', 16, 1);
        rollback transaction;
        return;
    end

    -- Si pasa la validación, efectúa el insert o update normalmente
    if exists (select * from deleted)
    begin
        update lote_vacuna 
        set codigo_vacuna = inserted.codigo_vacuna, no_lote = inserted.no_lote, fechaVencimiento = inserted.fechaVencimiento, 
        cantidad = inserted.cantidad
        from lote_vacuna inner join inserted on lote_vacuna.codigo_lote = inserted.codigo_lote;
    end
    else
    begin
        insert lote_vacuna (codigo_vacuna, no_lote, fechaVencimiento, cantidad)
        select codigo_vacuna, no_lote, fechaVencimiento, cantidad from inserted;
    end
end;