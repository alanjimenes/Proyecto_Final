use clinica;

delete from regvacuna;
delete from analisis;
delete from enfermedad_consulta;
delete from receta_medica;
delete from evaluacionfisica;
delete from consulta;
delete from cita;
delete from historial;
delete from lote_vacuna;
delete from enfermera;
delete from medico;
delete from cliente;
delete from persona;
delete from usuario;
delete from especialidad;
delete from vacuna;
delete from medicamento;
delete from tipo_analisis;
delete from enfermedad;

dbcc checkident ('especialidad', reseed, 1999);
dbcc checkident ('usuario', reseed, 2999);
dbcc checkident ('persona', reseed, 9999);
dbcc checkident ('historial', reseed, 49999);
dbcc checkident ('cita', reseed, 39999);
dbcc checkident ('consulta', reseed, 29999);
dbcc checkident ('evaluacionfisica', reseed, 0);
dbcc checkident ('medicamento', reseed, 999);
dbcc checkident ('receta_medica', reseed, 0);
dbcc checkident ('enfermedad', reseed, 19999);
dbcc checkident ('tipo_analisis', reseed, 99);
dbcc checkident ('analisis', reseed, 0);
dbcc checkident ('vacuna', reseed, 999);
dbcc checkident ('lote_vacuna', reseed, 9999);
dbcc checkident ('regvacuna', reseed, 39999);


insert into especialidad (nombre) values
('Medicina General'), ('Cardiología'), ('Pediatría'), ('Dermatología'), ('Neurología'),
('Ginecología'), ('Oftalmología'), ('Traumatología'), ('Endocrinología'), ('Neumología'),
('Gastroenterología'), ('Urología'), ('Psiquiatría'), ('Oncología'), ('Otorrinolaringología');

insert into usuario (nombreusuario, password, rol) values
('admin', 'admin123', 'Administrador'), ('asistente1', 'asist123', 'Asistente'),
('asistente2', 'asist456', 'Asistente'), ('asistente3', 'asist789', 'Asistente'),
('doctor.fernandez', 'med123', 'Médico'), ('doctor.sanchez', 'med456', 'Médico'),
('doctor.jimenez', 'med789', 'Médico'), ('doctor.ortiz', 'med111', 'Médico'),
('doctor.reyes', 'med222', 'Médico'), ('doctor.cruz', 'med333', 'Médico'),
('doctor.ramirez', 'med444', 'Médico'), ('doctor.mendez', 'med555', 'Médico'),
('doctor.castro', 'med666', 'Médico'), ('doctor.perez', 'med777', 'Médico'),
('doctor.guzman', 'med888', 'Médico'), ('enfermera.marga', 'enf123', 'Enfermera');

insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero) values
('1990-01-15', 'Juan', 'Pérez', '001-1234567-1', '809-555-1001', 1, 'Av. Independencia #101, Santo Domingo', 'Masculino'),
('1988-03-22', 'María', 'Rodríguez', '001-1234567-2', '809-555-1002', 1, 'Av. México #202, Santo Domingo', 'Femenino'),
('1995-07-10', 'Carlos', 'Martínez', '001-1234567-3', '809-555-1003', 1, 'Calle Duarte #303, Santo Domingo', 'Masculino'),
('2000-02-18', 'Ana', 'Gómez', '001-1234567-4', '809-555-1004', 1, 'Calle Las Américas #404, Santo Domingo', 'Femenino'),
('1985-11-30', 'Luis', 'Hernández', '001-1234567-5', '809-555-1005', 1, 'Av. John F. Kennedy #505, Santo Domingo', 'Masculino'),
('1992-06-05', 'Sofía', 'Castillo', '001-1234567-6', '809-555-1006', 1, 'Av. Sabana Larga #606, Santo Domingo', 'Femenino'),
('1979-09-14', 'Pedro', 'Santos', '001-1234567-7', '809-555-1007', 1, 'Calle El Sol #707, Santo Domingo', 'Masculino'),
('1998-12-25', 'Laura', 'Ramírez', '001-1234567-8', '809-555-1008', 1, 'Av. 27 de Febrero #808, Santo Domingo', 'Femenino'),
('1987-04-09', 'Miguel', 'Torres', '001-1234567-9', '809-555-1009', 1, 'Av. Abraham Lincoln #909, Santo Domingo', 'Masculino'),
('1993-10-17', 'Carmen', 'Díaz', '001-1234567-0', '809-555-1010', 1, 'Calle Wenceslao Álvarez #110, Santo Domingo', 'Femenino'),
('1982-03-11', 'Roberto', 'Morales', '001-2234567-1', '809-555-1011', 1, 'Av. Charles de Gaulle #111, Santo Domingo', 'Masculino'),
('1996-08-21', 'Patricia', 'Vargas', '001-2234567-2', '809-555-1012', 1, 'Calle José Contreras #112, Santo Domingo', 'Femenino'),
('1975-01-28', 'Fernando', 'Núñez', '001-2234567-3', '809-555-1013', 1, 'Av. Luperón #113, Santo Domingo', 'Masculino'),
('1999-05-16', 'Gabriela', 'Medina', '001-2234567-4', '809-555-1014', 1, 'Calle Venezuela #114, Santo Domingo', 'Femenino'),
('1991-12-03', 'Diego', 'Herrera', '001-2234567-5', '809-555-1015', 1, 'Av. San Vicente de Paúl #115, Santo Domingo', 'Masculino'),
('1978-02-10', 'Alejandro', 'Fernández', '001-3234567-1', '809-555-2001', 1, 'Av. Winston Churchill #201, Santo Domingo', 'Masculino'),
('1983-06-19', 'Beatriz', 'Sánchez', '001-3234567-2', '809-555-2002', 1, 'Av. Sarasota #202, Santo Domingo', 'Femenino'),
('1976-09-27', 'Ricardo', 'Jiménez', '001-3234567-3', '809-555-2003', 1, 'Av. Gustavo Mejía Ricart #203, Santo Domingo', 'Masculino'),
('1985-11-04', 'Mónica', 'Ortiz', '001-3234567-4', '809-555-2004', 1, 'Calle Tiradentes #204, Santo Domingo', 'Femenino'),
('1979-07-13', 'Daniel', 'Reyes', '001-3234567-5', '809-555-2005', 1, 'Av. Rómulo Betancourt #205, Santo Domingo', 'Masculino'),
('1981-03-30', 'Verónica', 'Cruz', '001-3234567-6', '809-555-2006', 1, 'Av. Núñez de Cáceres #206, Santo Domingo', 'Femenino'),
('1974-12-22', 'Óscar', 'Ramírez', '001-3234567-7', '809-555-2007', 1, 'Calle Los Prados #207, Santo Domingo', 'Masculino'),
('1988-05-08', 'Natalia', 'Méndez', '001-3234567-8', '809-555-2008', 1, 'Av. Anacaona #208, Santo Domingo', 'Femenino'),
('1980-10-15', 'Esteban', 'Castro', '001-3234567-9', '809-555-2009', 1, 'Av. Pedro Henríquez Ureña #209, Santo Domingo', 'Masculino'),
('1986-01-31', 'Juliana', 'Pérez', '001-3234567-0', '809-555-2010', 1, 'Calle Hostos #210, Santo Domingo', 'Femenino'),
('1977-04-18', 'Manuel', 'Guzmán', '001-4234567-1', '809-555-2011', 1, 'Av. Máximo Gómez #211, Santo Domingo', 'Masculino'),
('1984-08-26', 'Claudia', 'Santos', '001-4234567-2', '809-555-2012', 1, 'Calle Arzobispo Meriño #212, Santo Domingo', 'Femenino'),
('1972-06-12', 'Sergio', 'Flores', '001-4234567-3', '809-555-2013', 1, 'Calle Las Damas #213, Santo Domingo', 'Masculino'),
('1989-09-07', 'Adriana', 'Valdez', '001-4234567-4', '809-555-2014', 1, 'Av. Bolívar #214, Santo Domingo', 'Femenino'),
('1982-11-23', 'Héctor', 'Peña', '001-4234567-5', '809-555-2015', 1, 'Calle Cayetano Germosén #215, Santo Domingo', 'Masculino'),
('1980-05-10', 'Margarita', 'Rosario', '001-8888888-1', '809-555-4001', 1, 'Calle Moca #1, Santo Domingo', 'Femenino');

-- Del 10000 al 10014 (Clientes: 15 personas)
insert into cliente (codigo_persona, numexpediente, enfermo, antecedentes) values
(10000, 'EXP-10000', 0, 'Sin antecedentes patológicos relevantes.'),
(10001, 'EXP-10001', 1, 'Historial familiar de diabetes e hipertensión.'),
(10002, 'EXP-10002', 0, 'Alergia a la penicilina.'),
(10003, 'EXP-10003', 1, 'Diagnóstico previo de resistencia a la insulina.'),
(10004, 'EXP-10004', 1, 'Asma diagnosticada en la infancia.'),
(10005, 'EXP-10005', 0, 'Sin antecedentes de importancia.'),
(10006, 'EXP-10006', 1, 'Migrañas frecuentes diagnosticadas.'),
(10007, 'EXP-10007', 0, 'Dermatitis por contacto ocasional.'),
(10008, 'EXP-10008', 1, 'Fumador crónico, antecedentes de cardiopatía.'),
(10009, 'EXP-10009', 0, 'Rinitis alérgica estacional.'),
(10010, 'EXP-10010', 1, 'Tratamiento previo para el hipotiroidismo.'),
(10011, 'EXP-10011', 0, 'Osteopenia reportada en 2024.'),
(10012, 'EXP-10012', 1, 'Infecciones urinarias recurrentes.'),
(10013, 'EXP-10013', 0, 'Cirugía refractiva en 2021.'),
(10014, 'EXP-10014', 1, 'Crisis convulsivas en tratamiento neurológico.');

-- Del 10015 al 10029 (Médicos: 15 personas)
insert into medico (codigo_persona, codigo_usuario, codigo_especialidad, maxcitaspordia) values
(10015, 3004, 2000, 20), (10016, 3005, 2001, 15), (10017, 3006, 2002, 18),
(10018, 3007, 2003, 15), (10019, 3008, 2004, 12), (10020, 3009, 2005, 15),
(10021, 3010, 2006, 20), (10022, 3011, 2007, 18), (10023, 3012, 2008, 15),
(10024, 3013, 2009, 12), (10025, 3014, 2010, 15), (10026, null, 2011, 10),
(10027, null, 2012, 12), (10028, null, 2013, 10), (10029, null, 2014, 15);

-- 10030 (Enfermera)
insert into enfermera (codigo_persona, codigo_usuario, turno) values
(10030, 3015, 'Mañana');

insert into historial (codigo_cliente) values
(10000), (10001), (10002), (10003), (10004), (10005),
(10006), (10007), (10008), (10009), (10010), (10011),
(10012), (10013), (10014);

insert into enfermedad (activo, nombre, vigilancia, descripcion) values
(1, 'Hipertensión Arterial', 1, 'Presión arterial persistentemente elevada.'),
(1, 'Diabetes Mellitus Tipo 2', 1, 'Trastorno metabólico con glucosa elevada.'),
(1, 'Gripe Estacional', 0, 'Infección viral aguda respiratoria.'),
(1, 'Resfriado Común', 0, 'Infección viral leve.'),
(1, 'Asma Bronquial', 1, 'Inflamación crónica de vías respiratorias.'),
(1, 'Obesidad', 1, 'Acumulación excesiva de grasa.'),
(1, 'Gastritis', 0, 'Inflamación de la mucosa gástrica.'),
(1, 'Colesterol Alto', 1, 'Niveles elevados de lípidos.'),
(1, 'Migraña', 0, 'Cefalea intensa recurrente.'),
(1, 'Artritis', 1, 'Inflamación articular.'),
(1, 'Dermatitis', 0, 'Afección cutánea común.'),
(1, 'Hipotiroidismo', 1, 'Déficit de hormona tiroidea.'),
(1, 'Alergia Respiratoria', 0, 'Reacción a alérgenos ambientales.'),
(1, 'Enfermedad Renal Crónica', 1, 'Pérdida gradual de función renal.'),
(1, 'Conjuntivitis', 0, 'Inflamación conjuntival.'),
(1, 'Insuficiencia Cardíaca', 1, 'Incapacidad de bombeo cardíaco.'),
(1, 'Sinusitis', 0, 'Inflamación de senos paranasales.'),
(1, 'Osteoporosis', 1, 'Disminución de densidad ósea.'),
(1, 'Infección Urinaria', 0, 'Infección en el sistema urinario.'),
(1, 'Epilepsia', 1, 'Trastorno convulsivo cerebral.');

insert into tipo_analisis (nombre, descripcion) values
('Hemograma Completo', 'Evaluación general de salud.'),
('Perfil Lipídico', 'Medición de colesterol y triglicéridos.'),
('Glucosa en Sangre', 'Control de azúcar.'),
('Prueba de Función Renal', 'Niveles de urea y creatinina.'),
('Examen General de Orina', 'Análisis físico y químico.'),
('Perfil Tiroideo (TSH, T4)', 'Función tiroidea.');

insert into medicamento (nombre, concentracion, descripcion) values
('Paracetamol', '500 mg', 'Analgésico y antipirético.'),
('Ibuprofeno', '400 mg', 'Antiinflamatorio.'),
('Amoxicilina', '500 mg', 'Antibiótico.'),
('Losartán', '50 mg', 'Antihipertensivo.'),
('Metformina', '850 mg', 'Hipoglucemiante oral.'),
('Omeprazol', '20 mg', 'Protector gástrico.'),
('Loratadina', '10 mg', 'Antihistamínico.'),
('Salbutamol', '100 mcg', 'Broncodilatador.');

insert into consulta (fechaconsulta, sintomas, diagnostico, addresumen, codigo_medico, codigo_cliente) values
('2026-07-05 08:30:00', 'Dolor de cabeza y mareos.', 'Hipertensión Arterial', 1, 10015, 10000),
('2026-07-06 09:00:00', 'Tos persistente y fiebre.', 'Gripe Estacional', 1, 10015, 10001),
('2026-07-08 10:30:00', 'Dolor abdominal y acidez.', 'Gastritis', 1, 10025, 10002),
('2026-07-10 11:00:00', 'Sed excesiva y cansancio.', 'Diabetes Mellitus Tipo 2', 1, 10023, 10003),
('2026-07-12 08:15:00', 'Dificultad para respirar.', 'Asma Bronquial', 1, 10024, 10004),
('2026-07-15 09:45:00', 'Dolor articular en rodillas.', 'Artritis', 1, 10022, 10005),
('2026-07-17 13:00:00', 'Cefalea intensa y fotofobia.', 'Migraña', 1, 10019, 10006),
('2026-07-20 14:30:00', 'Irritación y picazón en piel.', 'Dermatitis', 1, 10018, 10007),
('2026-07-22 08:30:00', 'Opresión torácica y fatiga.', 'Insuficiencia Cardíaca', 1, 10016, 10008),
('2026-07-25 10:00:00', 'Dolor facial y secreción.', 'Sinusitis', 1, 10024, 10009),
('2026-08-01 09:15:00', 'Aumento de peso y piel seca.', 'Hipotiroidismo', 1, 10023, 10010),
('2026-08-03 11:30:00', 'Dolor lumbar mecánico.', 'Osteoporosis', 1, 10022, 10011),
('2026-08-05 15:00:00', 'Ardor al orinar.', 'Infección Urinaria', 1, 10026, 10012),
('2026-08-07 09:30:00', 'Enrojecimiento ocular.', 'Conjuntivitis', 1, 10021, 10013),
('2026-08-10 10:45:00', 'Episodio convulsivo.', 'Epilepsia', 1, 10019, 10014);

insert into evaluacionfisica (codigo_cons, temperatura, frecuenciacardiaca, presionarterial, peso, talla) values
(30000, 36.5, 78, '140/90', 82.5, 1.75), (30001, 38.2, 92, '120/80', 65.0, 1.62),
(30002, 36.8, 70, '115/75', 74.0, 1.70), (30003, 36.6, 84, '130/85', 90.5, 1.68),
(30004, 37.0, 88, '120/80', 70.2, 1.72), (30005, 36.7, 76, '125/80', 68.0, 1.60),
(30006, 36.9, 80, '110/70', 58.5, 1.58), (30007, 36.6, 72, '120/75', 62.0, 1.65),
(30008, 36.4, 96, '145/95', 85.0, 1.74), (30009, 37.8, 85, '120/80', 77.0, 1.80),
(30010, 36.2, 64, '110/70', 80.0, 1.63), (30011, 36.5, 75, '130/80', 55.0, 1.55),
(30012, 37.5, 82, '118/78', 64.5, 1.67), (30013, 36.6, 70, '120/80', 73.0, 1.71),
(30014, 36.7, 78, '125/82', 79.0, 1.76);

insert into receta_medica (codigo_cons, codigo_medicamento, frecuencia, duracion, dosis, descripcion) values
(30000, 1003, 'Cada 24 horas', '30 días', '50 mg', 'Tomar una tableta en las mañanas.'),
(30001, 1000, 'Cada 8 horas', '5 días', '500 mg', 'Tomar en caso de fiebre.'),
(30002, 1005, 'Cada 24 horas', '14 días', '20 mg', 'Tomar en ayunas.'),
(30003, 1004, 'Cada 12 horas', '30 días', '850 mg', 'Con alimentos.'),
(30004, 1007, 'Cada 6 horas', '30 días', '2 inhalaciones', 'Usar espaciador.'),
(30005, 1001, 'Cada 12 horas', '7 días', '400 mg', 'Después de comer.'),
(30006, 1000, 'Cada 8 horas', '3 días', '500 mg', 'Para cefalea.'),
(30007, 1006, 'Cada 24 horas', '10 días', '10 mg', 'Antes de dormir.'),
(30008, 1003, 'Cada 24 horas', '30 días', '50 mg', 'Mantenimiento.'),
(30009, 1002, 'Cada 8 horas', '7 días', '500 mg', 'Completar dosis.'),
(30010, 1000, 'Cada 8 horas', '5 días', '500 mg', 'Sintomático.'),
(30011, 1001, 'Cada 12 horas', '5 días', '400 mg', 'Con agua.'),
(30012, 1002, 'Cada 8 horas', '7 días', '500 mg', 'Infección.'),
(30013, 1006, 'Cada 24 horas', '5 días', '10 mg', 'Ocular.'),
(30014, 1000, 'Cada 8 horas', '3 días', '500 mg', 'Postictal.');

insert into enfermedad_consulta (codigo_enfermedad, codigo_cons) values
(20000, 30000), (20002, 30001), (20006, 30002), (20001, 30003), (20004, 30004),
(20009, 30005), (20008, 30006), (20010, 30007), (20015, 30008), (20016, 30009),
(20011, 30010), (20017, 30011), (20018, 30012), (20014, 30013), (20019, 30014);

insert into analisis (codigo_cons, codigo_tipo, fechaorden, fecharesultado, estado, resultado) values
(30000, 101, '2026-07-05 09:00:00', '2026-07-06 14:00:00', 'Completado', 'Colesterol total: 240 mg/dL.'),
(30001, 100, '2026-07-06 09:30:00', '2026-07-07 11:00:00', 'Completado', 'Leucocitosis leve.'),
(30003, 102, '2026-07-10 11:30:00', '2026-07-11 10:00:00', 'Completado', 'Glucosa basal: 165 mg/dL.'),
(30008, 103, '2026-07-22 09:00:00', '2026-07-23 15:00:00', 'Completado', 'Creatinina normal.'),
(30010, 105, '2026-08-01 10:00:00', '2026-08-03 12:00:00', 'Completado', 'TSH elevada.'),
(30012, 104, '2026-08-05 15:30:00', '2026-08-06 09:00:00', 'Completado', 'Bacteriuria presente.');

insert into cita (codigo_medico, codigo_cliente, fechacita, estado, motivo) values
(10015, 10000, '2026-07-05 08:00:00', 'Atendida', 'Control de presión arterial'),
(10015, 10001, '2026-07-06 09:00:00', 'Atendida', 'Evaluación de síntomas gripales'),
(10016, 10002, '2026-07-08 10:00:00', 'Atendida', 'Evaluación cardiológica inicial'),
(10017, 10003, '2026-07-10 11:00:00', 'Atendida', 'Control metabólico y glucosa'),
(10018, 10004, '2026-07-12 08:30:00', 'Atendida', 'Evaluación dermatológica'),
(10019, 10005, '2026-07-15 09:30:00', 'Atendida', 'Control por dolores articulares'),
(10020, 10006, '2026-07-17 10:30:00', 'Atendida', 'Consulta por migraña crónica'),
(10021, 10007, '2026-07-20 11:30:00', 'Atendida', 'Revisión por dermatitis alérgica'),
(10022, 10008, '2026-07-22 08:00:00', 'Atendida', 'Evaluación de dolor torácico'),
(10023, 10009, '2026-07-25 09:00:00', 'Atendida', 'Control por sinusitis aguda'),
(10024, 10010, '2026-08-01 10:00:00', 'Atendida', 'Seguimiento por hipotiroidismo'),
(10025, 10011, '2026-08-03 11:00:00', 'Atendida', 'Evaluación por dolor lumbar'),
(10026, 10012, '2026-08-05 08:30:00', 'Atendida', 'Consulta por síntomas urinarios'),
(10027, 10013, '2026-08-07 09:30:00', 'Atendida', 'Revisión oftalmológica y conjuntivitis'),
(10028, 10014, '2026-08-10 10:30:00', 'Atendida', 'Control neurológico por convulsiones'),
(10015, 10000, '2026-08-15 08:00:00', 'Atendida', 'Seguimiento de cefalea frecuente'),
(10016, 10003, '2026-08-18 09:00:00', 'Pendiente', 'Control de niveles de glucosa'),
(10018, 10005, '2026-08-20 10:00:00', 'Pendiente', 'Monitoreo de presión arterial'),
(10019, 10007, '2026-08-22 11:00:00', 'Cancelada', 'Control digestivo por gastritis'),
(10022, 10008, '2026-08-25 08:00:00', 'Pendiente', 'Revisión por cuadro respiratorio'),
(10023, 10009, '2026-09-01 09:00:00', 'Atendida', 'Control médico general'),
(10024, 10010, '2026-09-03 10:00:00', 'Pendiente', 'Seguimiento dermatológico'),
(10025, 10011, '2026-09-05 11:00:00', 'Atendida', 'Control cardiológico preventivo'),
(10026, 10012, '2026-09-08 08:30:00', 'Pendiente', 'Evaluación neurológica general'),
(10015, 10013, '2026-10-01 09:30:00', 'Pendiente', 'Cita de control trimestral'),
(10016, 10014, '2026-10-02 10:30:00', 'Pendiente', 'Seguimiento de rutina general'),
(10018, 10001, '2026-10-03 11:30:00', 'Pendiente', 'Evaluación médica programada'),
(10019, 10002, '2026-10-05 08:00:00', 'Pendiente', 'Control de especialidad'),
(10020, 10004, '2026-10-06 09:00:00', 'Pendiente', 'Revisión periódica de salud'),
(10021, 10006, '2026-10-08 10:00:00', 'Pendiente', 'Cita subsecuente de control');

insert into vacuna (nombre, descripcion, activo) values
('Hepatitis B', 'Vacuna para prevenir infección por hepatitis B.', 1),
('Influenza', 'Vacuna anual influenza estacional.', 1),
('Tétanos', 'Vacuna tétanos.', 1),
('Fiebre Amarilla', 'Vacuna fiebre amarilla.', 1),
('Hepatitis A', 'Vacuna hepatitis A.', 1),
('COVID-19', 'Vacuna COVID-19.', 1),
('Neumococo', 'Vacuna neumococo.', 1),
('Varicela', 'Vacuna varicela.', 1),
('Sarampión', 'Vacuna sarampión.', 1),
('Rubéola', 'Vacuna rubéola.', 1),
('Paperas', 'Vacuna paperas.', 1),
('Polio', 'Vacuna polio.', 1),
('Rotavirus', 'Vacuna rotavirus.', 1),
('Meningococo', 'Vacuna meningococo.', 1),
('VPH', 'Vacuna VPH.', 1);

insert into lote_vacuna (codigo_vacuna, no_lote, fechavencimiento, cantidad) values
(1000, 'LOTE-HB-01', '2027-12-31', 100), (1001, 'LOTE-INF-01', '2027-12-31', 100),
(1002, 'LOTE-TET-01', '2027-12-31', 100), (1003, 'LOTE-FA-01', '2027-12-31', 100),
(1004, 'LOTE-HA-01', '2027-12-31', 100), (1005, 'LOTE-COV-01', '2027-12-31', 100),
(1006, 'LOTE-NEU-01', '2027-12-31', 100), (1007, 'LOTE-VAR-01', '2027-12-31', 100),
(1008, 'LOTE-SAR-01', '2027-12-31', 100), (1009, 'LOTE-RUB-01', '2027-12-31', 100),
(1010, 'LOTE-PAP-01', '2027-12-31', 100), (1011, 'LOTE-POL-01', '2027-12-31', 100),
(1012, 'LOTE-ROT-01', '2027-12-31', 100), (1013, 'LOTE-MEN-01', '2027-12-31', 100),
(1014, 'LOTE-VPH-01', '2027-12-31', 100);

insert into regvacuna (codigo_cliente, codigo_lote, codigo_enfermera, fecha, aplicada) values
(10000, 10000, 10030, '2025-01-15 09:00:00', 1), (10000, 10001, 10030, '2026-01-20 09:00:00', 1),
(10001, 10001, 10030, '2026-01-21 10:00:00', 1), (10001, 10005, 10030, '2025-11-10 10:00:00', 1),
(10002, 10002, 10030, '2024-08-15 11:00:00', 1), (10002, 10006, 10030, '2025-10-20 11:00:00', 1),
(10003, 10007, 10030, '2025-03-12 08:30:00', 1), (10003, 10008, 10030, '2025-04-12 08:30:00', 1),
(10004, 10001, 10030, '2026-01-25 09:30:00', 1), (10004, 10002, 10030, '2025-06-15 09:30:00', 1),
(10005, 10003, 10030, '2025-07-20 10:00:00', 1), (10005, 10005, 10030, '2025-12-20 10:00:00', 1),
(10006, 10004, 10030, '2025-05-18 11:00:00', 1), (10006, 10009, 10030, '2025-05-18 11:00:00', 1),
(10007, 10001, 10030, '2026-02-01 08:00:00', 1), (10007, 10010, 10030, '2025-03-15 08:00:00', 1),
(10008, 10006, 10030, '2025-09-10 09:00:00', 1), (10008, 10002, 10030, '2024-09-10 09:00:00', 1),
(10009, 10000, 10030, '2025-02-14 10:00:00', 1), (10009, 10001, 10030, '2026-01-30 10:00:00', 1),
(10010, 10006, 10030, '2025-08-22 11:00:00', 1), (10010, 10003, 10030, '2024-08-22 11:00:00', 1),
(10011, 10007, 10030, '2025-04-20 08:30:00', 1), (10011, 10008, 10030, '2025-04-20 08:30:00', 1),
(10012, 10005, 10030, '2025-10-05 09:00:00', 1), (10012, 10001, 10030, '2026-02-05 09:00:00', 0),
(10013, 10011, 10030, '2025-06-10 10:00:00', 1), (10013, 10012, 10030, '2025-06-10 10:00:00', 1),
(10014, 10006, 10030, '2025-11-12 11:00:00', 1), (10014, 10013, 10030, '2025-11-12 11:00:00', 0);