use clinica;


-- =========================================================================
-- SECCIÓN: VISTAS
-- =========================================================================

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
go

/* Qué hace: Conecta la tabla lote_vacuna con la tabla vacuna. Filtra automáticamente para mostrar solo aquellos lotes 
que tengan existencia mayor a cero (cantidad > 0) y cuya fecha de vencimiento sea igual o posterior al día actual (>= GETDATE()). */

/* Para qué sirve: Evita que el personal médico seleccione vacunas vencidas o agotadas al momento de registrar una 
vacunación a un paciente, garantizando la seguridad clínica desde la base de datos. */

create view vw_inventario_vacunas_disponibles as
select lote_vacuna.codigo_lote, vacuna.codigo_vacuna, vacuna.nombre as nombre_vacuna, lote_vacuna.no_lote, 
lote_vacuna.cantidad, lote_vacuna.fechavencimiento
from lote_vacuna
inner join vacuna on lote_vacuna.codigo_vacuna = vacuna.codigo_vacuna
where lote_vacuna.cantidad > 0 and lote_vacuna.fechavencimiento >= getdate();
go


-- =========================================================================
-- SECCIÓN: ANÁLISIS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION) para insertar un nuevo registro en la tabla analisis. 
Captura los errores con TRY/CATCH; si todo es exitoso guarda los cambios (COMMIT), si algo falla revierte la operación (ROLLBACK). */

/* Para qué sirve: Garantiza que la orden del análisis se registre de forma íntegra en la base de datos, 
evitando datos corruptos o a medias si el servidor parpadea en medio del proceso. */

create procedure sp_crear_analisis
    @codigo_cons int, 
    @codigo_tipo int, 
    @fechaorden datetime, 
    @fecharesultado datetime, 
    @estado varchar(50), 
    @resultado text
as
begin
    begin transaction;
    begin try
        insert into analisis (codigo_cons, codigo_tipo, fechaorden, fecharesultado, estado, resultado)
        values (@codigo_cons, @codigo_tipo, @fechaorden, @fecharesultado, @estado, @resultado);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Ejecuta un UPDATE sobre la tabla analisis buscando por su llave primaria (codigo_analisis), 
envuelto en una transacción para proteger la integridad de los datos de la clínica. */

/* Para qué sirve: Permite al laboratorista o al médico actualizar el estado del análisis y redactar el resultado 
definitivo una vez que las pruebas estén completadas. */

create procedure sp_editar_analisis
    @codigo_analisis int,
    @codigo_cons int, 
    @codigo_tipo int, 
    @fechaorden datetime, 
    @fecharesultado datetime, 
    @estado varchar(50), 
    @resultado text
as
begin
    begin transaction;
    begin try
        update analisis 
        set analisis.codigo_cons = @codigo_cons, 
            analisis.codigo_tipo = @codigo_tipo, 
            analisis.fechaorden = @fechaorden, 
            analisis.fecharesultado = @fecharesultado, 
            analisis.estado = @estado, 
            analisis.resultado = @resultado 
        where analisis.codigo_analisis = @codigo_analisis;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Borra físicamente un registro específico de la tabla analisis basándose en el parámetro de entrada del código. Todo bajo una transacción segura. */

/* Para qué sirve: Permite descartar o anular órdenes de análisis que fueron creadas por error durante la consulta antes de llegar al laboratorio. */

create procedure sp_eliminar_analisis
    @codigo_analisis int
as
begin
    begin transaction;
    begin try
        delete from analisis 
        where analisis.codigo_analisis = @codigo_analisis;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Realiza una consulta SELECT cruzando (LEFT JOIN) la tabla analisis con tipo_analisis para extraer los datos descriptivos,
filtrando por el ID específico. */

/* Para qué sirve: Extrae todos los detalles exactos de un análisis individual para mostrarlos en los campos de texto de la ventana de 
modificación en la interfaz gráfica. */

create procedure sp_buscar_analisis
    @codigo_analisis int
as
begin
    select analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaorden, analisis.fecharesultado, analisis.estado, analisis.resultado, tipo_analisis.codigo_tipo, tipo_analisis.nombre as tipo_nombre, tipo_analisis.descripcion as tipo_desc 
    from analisis 
    left join tipo_analisis on analisis.codigo_tipo = tipo_analisis.codigo_tipo 
    where analisis.codigo_analisis = @codigo_analisis;
end;
go

/* Qué hace: Extrae todos los registros de la tabla analisis junto con el nombre y descripción del tipo de análisis asociado. */

/* Para qué sirve: Alimenta la tabla principal del sistema (JTable) donde el personal administrativo puede ver el listado global de
todos los análisis mandados a hacer en el hospital. */

create procedure sp_listar_analisis
as
begin
    select analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaorden, analisis.fecharesultado, analisis.estado, analisis.resultado, tipo_analisis.codigo_tipo, tipo_analisis.nombre as tipo_nombre, tipo_analisis.descripcion as tipo_desc 
    from analisis 
    left join tipo_analisis on analisis.codigo_tipo = tipo_analisis.codigo_tipo;
end;
go

/* Qué hace: Ejecuta un SELECT múltiple con INNER JOIN cruzando analisis, consulta, medico y persona para encontrar 
los análisis asociados exclusivamente a la cédula de un médico. */

/* Para qué sirve: Es el filtro de seguridad que garantiza que en el panel del médico solo aparezcan los análisis correspondientes a sus propios pacientes. */

create procedure sp_analisis_por_doctor
    @cedula_medico varchar(20)
as
begin
    select analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaorden, analisis.fecharesultado, analisis.estado, analisis.resultado, tipo_analisis.codigo_tipo, tipo_analisis.nombre as tipo_nombre, tipo_analisis.descripcion as tipo_desc 
    from analisis 
    left join tipo_analisis on analisis.codigo_tipo = tipo_analisis.codigo_tipo 
    inner join consulta on analisis.codigo_cons = consulta.codigo_cons 
    inner join medico on consulta.codigo_medico = medico.codigo_persona 
    inner join persona on medico.codigo_persona = persona.codigo_persona 
    where persona.cedula = @cedula_medico;
end;
go


-- =========================================================================
-- SECCIÓN: CITAS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Realiza la inserción de una nueva cita médica
buscando los códigos internos (codigo_persona) correspondientes a las cédulas del médico y del paciente. 
Si la operación es exitosa realiza COMMIT, de lo contrario revierte los cambios con ROLLBACK. */

/* Para qué sirve: Garantiza que la cita quede agendada de forma consistente vinculando correctamente las entidades de médico y
cliente sin riesgo de inconsistencias por desconexiones o errores a mitad del proceso. */

create procedure sp_crear_cita
    @cedula_medico varchar(20),
    @cedula_cliente varchar(20),
    @fechacita datetime,
    @estado varchar(50),
    @motivo text
as
begin
    begin transaction;
    begin try
        insert into cita (codigo_medico, codigo_cliente, fechacita, estado, motivo)
        values (
            (select persona.codigo_persona from persona where persona.cedula = @cedula_medico),
            (select persona.codigo_persona from persona where persona.cedula = @cedula_cliente),
            @fechacita,
            @estado,
            @motivo
        );

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Actualiza la fecha, hora y el médico asignado a una cita existente a partir de la cédula del nuevo médico y
el identificador de la cita, envuelto en una transacción. */

/* Para qué sirve: Permite reprogramar citas o reasignarlas a otro especialista garantizando la atomicidad en la actualización de los datos. */

create procedure sp_editar_cita
    @codigo_cita int,
    @nueva_fechahora datetime,
    @cedula_medico varchar(20)
as
begin
    begin transaction;
    begin try
        update cita 
        set cita.fechacita = @nueva_fechahora, 
            cita.codigo_medico = (select persona.codigo_persona from persona where persona.cedula = @cedula_medico)
        where cita.codigo_cita = @codigo_cita;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Modifica el campo de estado de la cita a 'Cancelada' basándose en su llave primaria, asegurando la operación mediante una transacción. */

/* Para qué sirve: Permite cancelar una cita manteniendo la trazabilidad histórica del registro sin eliminar físicamente la fila de la base de datos. */

create procedure sp_cancelar_cita
    @codigo_cita int
as
begin
    begin transaction;
    begin try
        update cita 
        set cita.estado = 'Cancelada' 
        where cita.codigo_cita = @codigo_cita;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: CLIENTES / PACIENTES
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta primero los datos en la tabla persona, 
recupera el ID generado mediante SCOPE_IDENTITY y evalúa si el expediente viene vacío para autogenerar uno con el formato "EXP-ID".
Luego inserta en la tabla cliente. Si ocurre un error revierte con ROLLBACK, de lo contrario confirma con COMMIT. */

/* Para qué sirve: Mantiene la integridad referencial al crear pacientes, delegando la lógica de autogenerar el número de expediente 
directamente al motor de la base de datos para no sobrecargar el backend en Java. */

create procedure sp_crear_cliente
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @cedula varchar(20),
    @telefono varchar(20),
    @estado bit,
    @direccion varchar(255),
    @genero varchar(50),
    @numexpediente varchar(50),
    @enfermo bit,
    @antecedentes text
as
begin
    begin transaction;
    begin try
        insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero)
        values (@fechanacimiento, @nombre, @apellido, @cedula, @telefono, @estado, @direccion, @genero);

        declare @id_persona int = scope_identity();
        declare @exp_final varchar(50) = @numexpediente;

        if (@exp_final is null or @exp_final = '' or @exp_final = 'N/A')
        begin
            set @exp_final = 'EXP-' + cast(@id_persona as varchar(10));
        end

        insert into cliente (codigo_persona, numexpediente, enfermo, antecedentes)
        values (@id_persona, @exp_final, @enfermo, @antecedentes);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE primero en los datos personales (tabla persona) y
luego en los datos médicos (tabla cliente), usando la cédula como filtro de búsqueda. */

/* Para qué sirve: Permite modificar el perfil de un paciente de forma atómica, asegurando que si falla la actualización de los antecedentes, 
tampoco se guarden los cambios de dirección o teléfono, previniendo historiales corruptos. */

create procedure sp_editar_cliente
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @telefono varchar(20),
    @direccion varchar(255),
    @estado bit,
    @genero varchar(50),
    @cedula varchar(20),
    @enfermo bit,
    @numexpediente varchar(50),
    @antecedentes text
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.fechanacimiento = @fechanacimiento, 
            persona.nombre = @nombre, 
            persona.apellido = @apellido, 
            persona.telefono = @telefono, 
            persona.direccion = @direccion, 
            persona.estado = @estado, 
            persona.genero = @genero 
        where persona.cedula = @cedula;

        update cliente 
        set cliente.enfermo = @enfermo, 
            cliente.numexpediente = @numexpediente, 
            cliente.antecedentes = @antecedentes 
        where cliente.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = @cedula);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Envuelve en una transacción segura una actualización sobre la tabla persona para cambiar la columna estado a 0. */

/* Para qué sirve: Realiza un borrado lógico (soft delete) del paciente en el sistema, asegurando que sus datos no aparezcan en 
listados activos pero se mantengan intactos para la bitácora del hospital. */

create procedure sp_eliminar_cliente
    @cedula varchar(20)
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.estado = 0 
        where persona.cedula = @cedula;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: CONSULTAS Y EVALUACIÓN FÍSICA
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Primero, inserta los datos principales de la consulta médica 
en la tabla consulta, luego, captura automáticamente el código generado (SCOPE_IDENTITY()) e inserta los signos vitales 
y datos antropométricos correspondientes en la tabla evaluacionFisica. 
Finalmente si todo sale bien, guarda los cambios (COMMIT), y si algo falla revierte todo (ROLLBACK). */

/* Para qué sirve: Garantiza la consistencia de los datos. En nuestro sistema médico, una consulta y su evaluación física van 
de la mano, este procedimiento evita que se guarde una consulta a medias si ocurre un error en el sistema a mitad del proceso. */

create procedure sp_registrar_consulta
    @codigo_medico int, 
    @codigo_cliente int, 
    @fechaconsulta datetime, 
    @sintomas text, 
    @diagnostico text,
    @temperatura float, 
    @frecuenciacardiaca int, 
    @presionarterial varchar(20), 
    @peso float, 
    @talla float
as
begin
    begin transaction;
    begin try
        insert into consulta (codigo_medico, codigo_cliente, fechaconsulta, sintomas, diagnostico, addresumen)
        values (@codigo_medico, @codigo_cliente, @fechaconsulta, @sintomas, @diagnostico, 1);

        declare @codigo_cons int = scope_identity();

        insert into evaluacionfisica (codigo_cons, temperatura, frecuenciacardiaca, presionarterial, peso, talla)
        values (@codigo_cons, @temperatura, @frecuenciacardiaca, @presionarterial, @peso, @talla);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta los parámetros vitales tomados al paciente en la tabla evaluacionfisica,
vinculándolos al código de la consulta médica. Finaliza con COMMIT o revierte con ROLLBACK si ocurre algún fallo. */

/* Para qué sirve: Registra de forma segura los signos vitales y datos antropométricos del paciente como parte fundamental del expediente en esa consulta específica. */

create procedure sp_crear_evaluacion_fisica
    @codigo_cons int,
    @temperatura float,
    @frecuenciacardiaca int,
    @presionarterial varchar(20),
    @peso float,
    @talla float
as
begin
    begin transaction;
    begin try
        insert into evaluacionfisica (codigo_cons, temperatura, frecuenciacardiaca, presionarterial, peso, talla)
        values (@codigo_cons, @temperatura, @frecuenciacardiaca, @presionarterial, @peso, @talla);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: ENFERMEDADES
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta un nuevo registro en la tabla enfermedad. Si todo sale bien realiza COMMIT,
de lo contrario revierte con ROLLBACK. */

/* Para qué sirve: Permite registrar una nueva patología o enfermedad en el catálogo del sistema médico de forma atómica y segura. */

create procedure sp_crear_enfermedad
    @nombre varchar(100),
    @descripcion text,
    @vigilancia bit
as
begin
    begin transaction;
    begin try
        insert into enfermedad (nombre, descripcion, vigilancia)
        values (@nombre, @descripcion, @vigilancia);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE en la tabla enfermedad buscando por su código primario. */

/* Para qué sirve: Permite actualizar los detalles o el estado de vigilancia epidemiológica de una enfermedad ya existente en el catálogo. */

create procedure sp_editar_enfermedad
    @codigo_enfermedad int,
    @nombre varchar(100),
    @descripcion text,
    @vigilancia bit
as
begin
    begin transaction;
    begin try
        update enfermedad 
        set enfermedad.nombre = @nombre, 
            enfermedad.descripcion = @descripcion, 
            enfermedad.vigilancia = @vigilancia 
        where enfermedad.codigo_enfermedad = @codigo_enfermedad;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Ejecuta un DELETE en la tabla enfermedad envuelto en una transacción para asegurar la operación. */

/* Para qué sirve: Permite eliminar físicamente una enfermedad del catálogo en caso de haber sido registrada por error. */

create procedure sp_eliminar_enfermedad
    @codigo_enfermedad int
as
begin
    begin transaction;
    begin try
        delete from enfermedad 
        where enfermedad.codigo_enfermedad = @codigo_enfermedad;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: ENFERMERAS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta primero los datos personales en la tabla persona y
recupera el ID generado usando SCOPE_IDENTITY(). Luego, inserta los datos específicos en la tabla enfermera. 
Finaliza con COMMIT o revierte con ROLLBACK si ocurre un error. */

/* Para qué sirve: Mantiene la integridad referencial al registrar una nueva enfermera, asegurando que ambos registros
(persona y enfermera) se creen de forma atómica y sin dejar datos huérfanos. */

create procedure sp_crear_enfermera
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @cedula varchar(20),
    @telefono varchar(20),
    @estado bit,
    @direccion varchar(255),
    @genero varchar(50),
    @turno varchar(50)
as
begin
    begin transaction;
    begin try
        insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero)
        values (@fechanacimiento, @nombre, @apellido, @cedula, @telefono, @estado, @direccion, @genero);

        declare @id_persona int = scope_identity();

        insert into enfermera (codigo_persona, turno)
        values (@id_persona, @turno);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE en la tabla persona y luego otro UPDATE en la tabla 
enfermera usando la cédula como filtro de búsqueda. */

/* Para qué sirve: Permite modificar el perfil de una enfermera de forma segura, garantizando que tanto sus datos
personales como su turno de trabajo se actualicen al mismo tiempo. */

create procedure sp_editar_enfermera
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @telefono varchar(20),
    @direccion varchar(255),
    @estado bit,
    @genero varchar(50),
    @cedula varchar(20),
    @turno varchar(50)
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.fechanacimiento = @fechanacimiento, 
            persona.nombre = @nombre, 
            persona.apellido = @apellido, 
            persona.telefono = @telefono, 
            persona.direccion = @direccion, 
            persona.estado = @estado, 
            persona.genero = @genero 
        where persona.cedula = @cedula;

        update enfermera 
        set enfermera.turno = @turno 
        where enfermera.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = @cedula);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Envuelve en una transacción un UPDATE sobre la tabla persona para cambiar la columna estado a 0 filtrando por la cédula. */

/* Para qué sirve: Realiza un borrado lógico de la enfermera en el sistema, lo que evita perder el historial de sus acciones mientras se 
le revoca el acceso y visibilidad en los listados activos. */

create procedure sp_desactivar_enfermera
    @cedula varchar(20)
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.estado = 0 
        where persona.cedula = @cedula;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: ESPECIALIDADES
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta el nombre de una nueva especialidad médica en el catálogo. 
Finaliza con COMMIT o revierte con ROLLBACK si ocurre un error. */

/* Para qué sirve: Mantiene un catálogo estandarizado de las áreas de la medicina que manejan los doctores de la clínica. */

create procedure sp_crear_especialidad
    @nombre varchar(100)
as
begin
    begin transaction;
    begin try
        insert into especialidad (nombre)
        values (@nombre);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE en la tabla especialidad, modificando el nombre de la misma buscando por su llave primaria. */

/* Para qué sirve: Permite corregir errores ortográficos o actualizar la nomenclatura de una especialidad médica ya registrada en el sistema. */

create procedure sp_editar_especialidad
    @codigo_especialidad int,
    @nombre varchar(100)
as
begin
    begin transaction;
    begin try
        update especialidad 
        set especialidad.nombre = @nombre 
        where especialidad.codigo_especialidad = @codigo_especialidad;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Ejecuta un DELETE en la tabla especialidad utilizando el identificador único envuelto en un bloque transaccional para la integridad. */

/* Para qué sirve: Permite remover especialidades del catálogo. Ojo: SQL Server bloqueará automáticamente esta acción si intentas borrar una especialidad
que ya está asignada a un médico gracias a las llaves foráneas. */

create procedure sp_eliminar_especialidad
    @codigo_especialidad int
as
begin
    begin transaction;
    begin try
        delete from especialidad 
        where especialidad.codigo_especialidad = @codigo_especialidad;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: LOTE DE VACUNAS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta un nuevo lote de vacunas en la tabla lote_vacuna con su fecha
de vencimiento y cantidad inicial. Si todo sale bien ejecuta COMMIT, de lo contrario revierte con ROLLBACK. */

/* Para qué sirve: Registra de manera segura y atómica el ingreso de un nuevo lote de vacunas al inventario de la clínica. */

create procedure sp_crear_lote_vacuna
    @codigo_vacuna int,
    @no_lote varchar(50),
    @fechavencimiento date,
    @cantidad int
as
begin
    begin transaction;
    begin try
        insert into lote_vacuna (codigo_vacuna, no_lote, fechavencimiento, cantidad)
        values (@codigo_vacuna, @no_lote, @fechavencimiento, @cantidad);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para actualizar los datos de un lote específico en la tabla lote_vacuna buscando por su llave primaria. */

/* Para qué sirve: Permite corregir errores de entrada (como una fecha de vencimiento mal digitada o una cantidad incorrecta) sin afectar el resto del inventario. */

create procedure sp_editar_lote_vacuna
    @codigo_lote int,
    @codigo_vacuna int,
    @no_lote varchar(50),
    @fechavencimiento date,
    @cantidad int
as
begin
    begin transaction;
    begin try
        update lote_vacuna 
        set lote_vacuna.codigo_vacuna = @codigo_vacuna, 
            lote_vacuna.no_lote = @no_lote, 
            lote_vacuna.fechavencimiento = @fechavencimiento, 
            lote_vacuna.cantidad = @cantidad 
        where lote_vacuna.codigo_lote = @codigo_lote;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Borra un registro de la tabla lote_vacuna utilizando su identificador, asegurando la consistencia mediante una transacción. */

/* Para qué sirve: Elimina un lote del sistema en caso de registro duplicado. Ojo: SQL Server bloqueará automáticamente esta acción si este 
lote ya tiene registros de vacunación vinculados por llave foránea. */

create procedure sp_eliminar_lote_vacuna
    @codigo_lote int
as
begin
    begin transaction;
    begin try
        delete from lote_vacuna 
        where lote_vacuna.codigo_lote = @codigo_lote;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: MEDICAMENTOS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta los datos de un nuevo medicamento en el catálogo. Finaliza con COMMIT o 
revierte con ROLLBACK en caso de error. */

/* Para qué sirve: Registra de manera segura un nuevo fármaco disponible para ser recetado posteriormente en las consultas de la clínica. */

create procedure sp_crear_medicamento
    @nombre varchar(100),
    @concentracion varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        insert into medicamento (nombre, concentracion, descripcion)
        values (@nombre, @concentracion, @descripcion);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE en la tabla medicamento buscando por su llave primaria. */

/* Para qué sirve: Permite modificar la información de un medicamento en caso de cambios en su presentación, gramaje o corrección de datos descriptivos. */

create procedure sp_editar_medicamento
    @codigo_medicamento int,
    @nombre varchar(100),
    @concentracion varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        update medicamento 
        set medicamento.nombre = @nombre, 
            medicamento.concentracion = @concentracion, 
            medicamento.descripcion = @descripcion 
        where medicamento.codigo_medicamento = @codigo_medicamento;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Ejecuta un DELETE protegido por un bloque transaccional sobre la tabla medicamento. */
/* Para qué sirve: Elimina un fármaco del catálogo. La base de datos bloqueará automáticamente esto por la llave foránea 
si el medicamento ya ha sido asignado en alguna receta médica. */

create procedure sp_eliminar_medicamento
    @codigo_medicamento int
as
begin
    begin transaction;
    begin try
        delete from medicamento 
        where medicamento.codigo_medicamento = @codigo_medicamento;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: MÉDICOS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta los datos personales del médico en la tabla persona y 
recupera el identificador generado (SCOPE_IDENTITY). Luego inserta los datos profesionales en la tabla medico, manejando el 
código de usuario como nulo si llega en cero. Si todo es correcto hace COMMIT, sino revierte con ROLLBACK. */

/* Para qué sirve: Mantiene la consistencia referencial al registrar un doctor en la clínica, garantizando que su perfil personal y
médico se creen juntos sin dejar registros huérfanos. */

create procedure sp_crear_medico
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @cedula varchar(20),
    @telefono varchar(20),
    @estado bit,
    @direccion varchar(255),
    @genero varchar(50),
    @codigo_usuario int,
    @codigo_especialidad int,
    @maxcitaspordia int
as
begin
    begin transaction;
    begin try
        insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero)
        values (@fechanacimiento, @nombre, @apellido, @cedula, @telefono, @estado, @direccion, @genero);

        declare @id_persona int = scope_identity();

        if (@codigo_usuario = 0)
        begin
            insert into medico (codigo_persona, codigo_usuario, codigo_especialidad, maxcitaspordia)
            values (@id_persona, null, @codigo_especialidad, @maxcitaspordia);
        end
        else
        begin
            insert into medico (codigo_persona, codigo_usuario, codigo_especialidad, maxcitaspordia)
            values (@id_persona, @codigo_usuario, @codigo_especialidad, @maxcitaspordia);
        end

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para ejecutar un UPDATE en la tabla persona y otro en la tabla medico usando la cédula como filtro de búsqueda. */

/* Para qué sirve: Permite modificar de forma atómica y segura el perfil de un médico, asegurando que tanto su información personal como su especialidad o 
límite de citas se actualicen simultáneamente. */

create procedure sp_editar_medico
    @fechanacimiento date,
    @nombre varchar(100),
    @apellido varchar(100),
    @telefono varchar(20),
    @direccion varchar(255),
    @estado bit,
    @genero varchar(50),
    @cedula varchar(20),
    @codigo_especialidad int,
    @maxcitaspordia int
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.fechanacimiento = @fechanacimiento, 
            persona.nombre = @nombre, 
            persona.apellido = @apellido, 
            persona.telefono = @telefono, 
            persona.direccion = @direccion, 
            persona.estado = @estado, 
            persona.genero = @genero 
        where persona.cedula = @cedula;

        update medico 
        set medico.codigo_especialidad = @codigo_especialidad, 
            medico.maxcitaspordia = @maxcitaspordia 
        where medico.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = @cedula);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Envuelve en una transacción un UPDATE sobre la tabla persona para cambiar la columna estado a 0 filtrando por la cédula. */

/* Para qué sirve: Realiza un borrado lógico del médico en el sistema, removiéndolo de las listas activas pero conservando su 
historial de recetas, consultas y diagnósticos intactos. */

create procedure sp_desactivar_medico
    @cedula varchar(20)
as
begin
    begin transaction;
    begin try
        update persona 
        set persona.estado = 0 
        where persona.cedula = @cedula;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: PERSONAS
-- =========================================================================

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
    select @codigo_persona = persona.codigo_persona 
    from persona 
    where persona.cedula = @cedula;

    begin transaction;
    begin try
        update persona 
        set persona.estado = 0 
        where persona.codigo_persona = @codigo_persona;

        update cita 
        set cita.estado = 'Cancelada' 
        where cita.codigo_cliente = @codigo_persona and cita.fechacita >= getdate() and cita.estado = 'Pendiente';

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Recibe una cédula como parámetro y actualiza directamente el estado de la persona a activo (estado = 1) 
en la tabla correspondiente, permitiéndole nuevamente el acceso al sistema. */

/*Para qué sirve: Automatiza el proceso de reactivar a un paciente o usuario de manera rápida y directa, habilitando su perfil 
para que pueda volver a utilizar los servicios clínicos y agendar citas. */

create procedure sp_activar_persona
    @cedula varchar(20) 
as
begin
    update persona 
    set persona.estado = 1 
    where persona.cedula = @cedula;
end;
go


-- =========================================================================
-- SECCIÓN: RECETAS MÉDICAS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta un nuevo registro en la tabla receta_medica vinculando 
la consulta con el medicamento y sus indicaciones. Si la operación es exitosa realiza COMMIT, de lo contrario revierte con ROLLBACK. */

/* Para qué sirve: Registra de forma segura la prescripción de un medicamento a un paciente dentro de una consulta médica específica. */

create procedure sp_crear_receta_medica
    @codigo_cons int,
    @codigo_medicamento int,
    @frecuencia varchar(100),
    @duracion varchar(100),
    @dosis varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        insert into receta_medica (codigo_cons, codigo_medicamento, frecuencia, duracion, dosis, descripcion)
        values (@codigo_cons, @codigo_medicamento, @frecuencia, @duracion, @dosis, @descripcion);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: TIPOS DE ANÁLISIS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta un nuevo registro en la tabla tipo_analisis con su nombre y descripción. 
Si todo sale bien ejecuta COMMIT, de lo contrario revierte con ROLLBACK. */

/* Para qué sirve: Registra de manera segura un nuevo tipo de prueba de laboratorio en el catálogo del sistema médico. */

create procedure sp_crear_tipo_analisis
    @nombre varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        insert into tipo_analisis (nombre, descripcion)
        values (@nombre, @descripcion);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para actualizar los datos de un tipo de análisis en la tabla tipo_analisis buscando por su llave primaria. */

/* Para qué sirve: Permite modificar el nombre o la descripción de un tipo de examen de laboratorio si hubo un error al registrarlo o si cambian sus especificaciones. */

create procedure sp_editar_tipo_analisis
    @codigo_tipo int,
    @nombre varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        update tipo_analisis 
        set tipo_analisis.nombre = @nombre, 
            tipo_analisis.descripcion = @descripcion 
        where tipo_analisis.codigo_tipo = @codigo_tipo;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Borra un registro de la tabla tipo_analisis utilizando su identificador único, protegido por un bloque transaccional. */

/* Para qué sirve: Elimina un tipo de prueba del catálogo general en caso de que se haya duplicado o ya no se ofrezca en la clínica. */

create procedure sp_eliminar_tipo_analisis
    @codigo_tipo int
as
begin
    begin transaction;
    begin try
        delete from tipo_analisis 
        where tipo_analisis.codigo_tipo = @codigo_tipo;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: USUARIOS
-- =========================================================================

/* Qué hace: Utiliza una transacción (BEGIN TRANSACTION). Inserta el nuevo usuario y, si se le pasa una cédula, busca a qué tabla de personal 
(medico o enfermera) pertenece para enlazar automáticamente el código de usuario generado (SCOPE_IDENTITY). Termina con COMMIT o revierte con ROLLBACK. */

/* Para qué sirve: Centraliza la creación de credenciales y su asignación al personal médico en un solo paso atómico, 
evitando que un doctor se quede sin poder iniciar sesión por un error de red. */

create procedure sp_crear_usuario
    @nombreusuario varchar(100),
    @password varchar(100),
    @rol varchar(50),
    @cedula varchar(20)
as
begin
    begin transaction;
    begin try
        insert into usuario (nombreusuario, password, rol)
        values (@nombreusuario, @password, @rol);

        declare @id_usuario int = scope_identity();

        if (@cedula is not null and @cedula != '')
        begin
            if (@rol = 'Medico' or @rol = 'Médico')
            begin
                update medico
                set medico.codigo_usuario = @id_usuario
                where medico.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = @cedula);
            end
            
            if (@rol = 'Enfermera')
            begin
                update enfermera
                set enfermera.codigo_usuario = @id_usuario
                where enfermera.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = @cedula);
            end
        end

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para actualizar el password y el rol de un usuario en la tabla usuario buscando por su nombre de usuario. */

/* Para qué sirve: Permite restablecer contraseñas o cambiar el nivel de privilegios de un empleado en el sistema de manera segura. */

create procedure sp_editar_usuario
    @nombreusuario varchar(100),
    @password varchar(100),
    @rol varchar(50)
as
begin
    begin transaction;
    begin try
        update usuario 
        set usuario.password = @password, 
            usuario.rol = @rol 
        where usuario.nombreusuario = @nombreusuario;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Ejecuta un DELETE protegido sobre la tabla usuario utilizando el nombre de usuario como filtro. */

/* Para qué sirve: Elimina las credenciales de acceso de un empleado que ya no labora en la clínica o que fue registrado por equivocación. */

create procedure sp_eliminar_usuario
    @nombreusuario varchar(100)
as
begin
    begin transaction;
    begin try
        delete from usuario 
        where usuario.nombreusuario = @nombreusuario;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go


-- =========================================================================
-- SECCIÓN: VACUNAS
-- =========================================================================

/* Qué hace: Utiliza una transacción (begin transaction). Inserta una nueva vacuna en el catálogo. 
Si todo sale bien ejecuta commit, de lo contrario revierte con rollback. */

/* Para qué sirve: Registra de manera segura un nuevo tipo de vacuna en el sistema del hospital. */

create procedure sp_crear_vacuna
    @nombre varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        insert into vacuna (nombre, descripcion)
        values (@nombre, @descripcion);

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para actualizar los datos de una vacuna en la tabla vacuna buscando por su llave primaria. */

/* Para qué sirve: Permite modificar el nombre o la descripción de la vacuna en caso de algún error ortográfico o cambio en su presentación clínica. */

create procedure sp_editar_vacuna
    @codigo_vacuna int,
    @nombre varchar(100),
    @descripcion text
as
begin
    begin transaction;
    begin try
        update vacuna 
        set vacuna.nombre = @nombre, 
            vacuna.descripcion = @descripcion 
        where vacuna.codigo_vacuna = @codigo_vacuna;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Borra un registro de la tabla vacuna utilizando su identificador único, protegido por un bloque transaccional. */

/* Para qué sirve: Elimina una vacuna del catálogo general. SQL Server bloqueará esta acción de forma automática si esta vacuna
ya tiene lotes registrados o pacientes vacunados con ella. */

create procedure sp_eliminar_vacuna
    @codigo_vacuna int
as
begin
    begin transaction;
    begin try
        delete from vacuna 
        where vacuna.codigo_vacuna = @codigo_vacuna;

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go

/* Qué hace: Inicia una transacción para insertar el registro de la aplicación de una vacuna a un paciente en la tabla regvacuna. 
Obtiene el id de la persona mediante una subconsulta a la cédula. */

/* Para qué sirve: Registra de manera segura cuándo, quién y a qué paciente se le aplicó una dosis específica, blindando el proceso ante fallos de conexión. */

create procedure sp_aplicar_vacuna
    @cedula_cliente varchar(20),
    @codigo_lote int,
    @codigo_enfermera int,
    @fecha datetime,
    @aplicada bit
as
begin
    begin transaction;
    begin try
        insert into regvacuna (codigo_cliente, codigo_lote, codigo_enfermera, fecha, aplicada)
        values (
            (select persona.codigo_persona from persona where persona.cedula = @cedula_cliente),
            @codigo_lote,
            @codigo_enfermera,
            @fecha,
            @aplicada
        );

        commit transaction;
    end try
    begin catch
        rollback transaction;
        throw;
    end catch
end;
go




-- =========================================================================
-- SECCIÓN: DISPARADORES (TRIGGERS)
-- =========================================================================

/* Qué hace: Es un disparador de tipo AFTER INSERT en la tabla regVacuna. Cada vez que se registra la aplicación de una vacuna a 
un paciente, el trigger detecta qué lote se usó y le resta exactamente 1 a la cantidad disponible en la tabla lote_vacuna. */

/* Para qué sirve: Automatiza el control del inventario. El personal de enfermería solo registra la vacunación y el sistema 
se encarga de descontar la dosis del almacén en tiempo real, evitando errores humanos de conteo manual. */
create trigger trg_descontar_stock_vacuna
on regvacuna
after insert
as
begin
    update lote_vacuna
    set lote_vacuna.cantidad = lote_vacuna.cantidad - 1
    from lote_vacuna 
    inner join inserted on lote_vacuna.codigo_lote = inserted.codigo_lote;
end;
go

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
    if exists (select 1 from inserted where inserted.fechavencimiento < getdate())
    begin
        raiserror('Error: No se puede registrar o actualizar un lote con una fecha de vencimiento pasada.', 16, 1);
        rollback transaction;
        return;
    end

    if exists (select * from deleted)
    begin
        update lote_vacuna 
        set lote_vacuna.codigo_vacuna = inserted.codigo_vacuna, 
            lote_vacuna.no_lote = inserted.no_lote, 
            lote_vacuna.fechavencimiento = inserted.fechavencimiento, 
            lote_vacuna.cantidad = inserted.cantidad
        from lote_vacuna 
        inner join inserted on lote_vacuna.codigo_lote = inserted.codigo_lote;
    end
    else
    begin
        insert into lote_vacuna (codigo_vacuna, no_lote, fechavencimiento, cantidad)
        select inserted.codigo_vacuna, inserted.no_lote, inserted.fechavencimiento, inserted.cantidad 
        from inserted;
    end
end;
go