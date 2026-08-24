create database Clinica;
use Clinica;

create table especialidad(
    codigo_especialidad int not null identity(2000,1),
    nombre varchar(50) not null,
    constraint pk_especialidad primary key (codigo_especialidad)
    );
create table usuario(
    codigo_usuario int not null identity(3000,1),
    nombreUsuario varchar(30) not null unique,
    password varchar(255),
    rol varchar(20),
    constraint pk_usuario primary key (codigo_usuario)
    );
create table persona(
    codigo_persona int not null identity(10000,1),
    fechaNacimiento date,
    nombre varchar(30) not null,
    apellido varchar(30) not null,
    cedula varchar(15) not null unique,
    telefono varchar(15) not null,
    estado bit default 0,
    direccion varchar(100),
    genero varchar(20),
    constraint pk_persona primary key (codigo_persona)
    );
create table cliente(
    codigo_persona int not null,
    numExpediente varchar(20),
    enfermo bit default 0,
    antecedentes text,
    constraint pk_cliente primary key (codigo_persona),
    constraint fk_cliente_persona foreign key (codigo_persona) references persona(codigo_persona)
    );
create table medico(
    codigo_persona int not null,
    codigo_usuario int,
    codigo_especialidad int,
    maxCitasPorDia int,
    constraint pk_medico primary key (codigo_persona),
    constraint fk_medico_persona foreign key (codigo_persona) references persona(codigo_persona),
    constraint fk_medico_usuario foreign key (codigo_usuario) references usuario(codigo_usuario),
    constraint fk_medico_especialidad foreign key (codigo_especialidad) references especialidad(codigo_especialidad)
    );
create table enfermera(
    codigo_persona int not null,
    codigo_usuario int,
    turno varchar(20),
    constraint pk_enfermera primary key (codigo_persona),
    constraint fk_enfermera_persona foreign key (codigo_persona) references persona(codigo_persona),
    constraint fk_enfermera_usuario foreign key (codigo_usuario) references usuario(codigo_usuario)
    );
create table historial(
    codigo_historial int not null identity(50000,1),
    codigo_cliente int not null,
    constraint pk_historial primary key (codigo_historial),
    constraint fk_historial_cliente foreign key (codigo_cliente) references cliente(codigo_persona)
    );
create table cita(
    codigo_cita int not null identity(40000,1),
    codigo_medico int not null,
    codigo_cliente int not null,
    fechaCita datetime not null,
    estado varchar(10) not null check (estado = 'Pendiente' OR estado = 'Atendida' OR estado = 'Cancelada'),
    motivo text not null,
    constraint pk_cita primary key (codigo_cita),
    constraint fk_cita_medico foreign key (codigo_medico) references medico(codigo_persona),
    constraint fk_cita_cliente foreign key (codigo_cliente) references cliente(codigo_persona)
    );
create table consulta(
    codigo_cons int not null identity(30000,1),
    codigo_medico int not null,
    codigo_cliente int not null,
    fechaConsulta datetime not null,
    sintomas text,
    diagnostico text,
    addResumen bit default 0,
    constraint pk_consulta primary key (codigo_cons),
    constraint fk_consulta_medico foreign key (codigo_medico) references medico(codigo_persona),
    constraint fk_consulta_cliente foreign key (codigo_cliente) references cliente(codigo_persona)
    );
create table evaluacionFisica(
    codigo_evaluacion int not null identity(1,1),
    codigo_cons int not null unique,
    temperatura float,
    frecuenciaCardiaca int,
    presionArterial varchar(20),
    peso float,
    talla float,
    constraint pk_evaluacionfisica primary key (codigo_evaluacion),
    constraint fk_evaluacion_consulta foreign key (codigo_cons) references consulta(codigo_cons)
    );
create table medicamento(
    codigo_medicamento int not null identity(1000,1),
    nombre varchar(100) not null,
    concentracion varchar(50),
    descripcion varchar(255),
    constraint pk_medicamento primary key (codigo_medicamento)
    );
create table receta_medica(
    codigo_rec int not null identity(1,1),
    codigo_cons int not null,
    codigo_medicamento int not null,
    frecuencia varchar(50),
    duracion varchar(50),
    dosis varchar(50),
    descripcion varchar(255),
    constraint pk_receta primary key (codigo_rec),
    constraint fk_receta_consulta foreign key (codigo_cons) references consulta(codigo_cons),
    constraint fk_receta_medicamento foreign key (codigo_medicamento) references medicamento(codigo_medicamento)
    );
create table enfermedad(
    codigo_enfermedad int not null identity(20000,1),
    activo bit default 1,
    nombre varchar(100) not null,
    vigilancia bit default 0,
    descripcion text,
    constraint pk_enfermedad primary key (codigo_enfermedad)
    );
create table enfermedad_consulta(
    codigo_enfermedad int not null,
    codigo_cons int not null,
    constraint pk_enfermedad_consulta primary key (codigo_enfermedad, codigo_cons),
    constraint fk_ec_enfermedad foreign key (codigo_enfermedad) references enfermedad(codigo_enfermedad),
    constraint fk_ec_consulta foreign key (codigo_cons) references consulta(codigo_cons)
    );
create table tipo_analisis(
    codigo_tipo int not null identity(100,1),
    nombre varchar(100) not null,
    descripcion varchar(255),
    constraint pk_tipo_analisis primary key (codigo_tipo)
    );
create table analisis(
    codigo_analisis int not null identity(1,1),
    codigo_cons int not null,
    codigo_tipo int not null,
    fechaOrden datetime not null,
    fechaResultado datetime,
    estado varchar(20),
    resultado text,
    constraint pk_analisis primary key (codigo_analisis),
    constraint fk_analisis_consulta foreign key (codigo_cons) references consulta(codigo_cons),
    constraint fk_analisis_tipo foreign key (codigo_tipo) references tipo_analisis(codigo_tipo)
    );
create table vacuna(
    codigo_vacuna int not null identity(1000,1),
    nombre varchar(50) not null,
    descripcion text not null,
    activo bit default 1,
    constraint pk_vacuna primary key (codigo_vacuna)
    );
create table lote_vacuna(
    codigo_lote int not null identity(10000,1),
    codigo_vacuna int not null,
    no_lote varchar(50) not null,
    fechaVencimiento date not null,
    cantidad int not null,
    constraint pk_lote_vacuna primary key (codigo_lote),
    constraint fk_lote_vacuna foreign key (codigo_vacuna) references vacuna(codigo_vacuna)
    );
create table regVacuna(
    codigo_reg int not null identity(40000,1),
    codigo_cliente int not null,
    codigo_lote int not null,
    codigo_enfermera int not null,
    fecha datetime,
    aplicada bit default 0,
    constraint pk_regvacuna primary key (codigo_reg),
    constraint fk_regvacuna_cliente foreign key (codigo_cliente) references cliente(codigo_persona),
    constraint fk_regvacuna_lote foreign key (codigo_lote) references lote_vacuna(codigo_lote),
    constraint fk_regvacuna_enfermera foreign key (codigo_enfermera) references enfermera(codigo_persona)
    );