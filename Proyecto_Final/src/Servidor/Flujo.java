package Servidor;

import logico.*;
import Servicios.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Flujo extends Thread {
    Socket nsfd;
    ObjectInputStream FlujoLectura = null;
    ObjectOutputStream FlujoEscritura = null;
    private UserService userService = new UserService();

    public Flujo(Socket sfd) {
        nsfd = sfd;
        try {
            FlujoEscritura = new ObjectOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
            FlujoEscritura.flush();

            FlujoLectura = new ObjectInputStream(new BufferedInputStream(sfd.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Error creando flujos: " + ioe);
        }
    }

    public void run() {
        if (FlujoLectura == null || FlujoEscritura == null) {
            try {
                if (nsfd != null) nsfd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        ClienteService clienteService = new ClienteService();
        MedicoService medicoService = new MedicoService();
        EnfermedadService enfermedadService = new EnfermedadService();
        EspecialidadService especialidadService = new EspecialidadService();
        CitaService citaService = new CitaService();
        ConsultaService consultaService = new ConsultaService();
        VacunaService vacunaService = new VacunaService();
        EnfermeraService enfermeraService = new EnfermeraService();
        LoteVacunaService loteVacunaService = new LoteVacunaService();
        EvaluacionFisicaService evaluacionFisicaService = new EvaluacionFisicaService();
        MedicamentoService medicamentoService = new MedicamentoService();
        RecetaMedicaService recetaMedicaService = new RecetaMedicaService();
        TipoAnalisisService tipoAnalisisService = new TipoAnalisisService();
        AnalisisService analisisService = new AnalisisService();

        try {
            while (true) {
                PaqueteDeDatos paquete = null;
                try {
                    paquete = (PaqueteDeDatos) FlujoLectura.readObject();
                } catch (EOFException e) {
                    break;
                }

                if (paquete == null)
                    break;

                String comando = paquete.getComando();

                switch (comando.toUpperCase()) {

                    // USUARIOS
                    case "LOGIN":
                        User login = (User) paquete.getObjeto();
                        User respuesta = userService.login(login.getNombreUsuario(), login.getPassword());
                        paquete.setRespuesta(respuesta);
                        break;

                    case "REG_USER":
                        User nuevo = (User) paquete.getObjeto();
                        if (userService.existeUsuario(nuevo.getNombreUsuario())) {
                            paquete.setRespuesta(false);
                        } else {
                            paquete.setRespuesta(userService.registrarUsuario(nuevo));
                        }
                        break;

                    case "LISTAR_USUARIOS":
                        paquete.setRespuesta(userService.listarUsuarios());
                        break;

                    case "ELIMINAR_USUARIO":
                        String usuario = (String) paquete.getObjeto();
                        paquete.setRespuesta(userService.eliminarUsuario(usuario));
                        break;

                    case "ACTUALIZAR_USUARIO":
                        User editar = (User) paquete.getObjeto();
                        paquete.setRespuesta(userService.actualizarUsuario(editar));
                        break;

                    // ENFERMEDADES
                    case "REG_ENFERMEDAD":
                        Enfermedad enf = (Enfermedad) paquete.getObjeto();
                        boolean exitoEnf = enfermedadService.agregarEnfermedad(enf);
                        paquete.setRespuesta(exitoEnf);
                        break;

                    case "UPDATE_ENFERMEDAD":
                        paquete.setRespuesta(false);
                        break;

                    case "LISTAR_ENFERMEDADES":
                        paquete.setRespuesta(enfermedadService.listarEnfermedades());
                        break;

                    case "FRECUENCIA_ENFERMEDADES":
                        paquete.setRespuesta(enfermedadService.getFrecuenciaEnfermedades());
                        break;

                    case "TOP_5_ENFERMEDADES":
                        paquete.setRespuesta(enfermedadService.getTop5Enfermedades());
                        break;

                    case "ENFERMEDADES_DE_CLIENTE":
                        String expEnf = (String) paquete.getObjeto();
                        paquete.setRespuesta(enfermedadService.getEnfermedadesDeCliente(expEnf));
                        break;

                    case "CLIENTES_POR_ENFERMEDAD":
                        String nomEnf = (String) paquete.getObjeto();
                        paquete.setRespuesta(enfermedadService.getClientesPorEnfermedad(nomEnf));
                        break;

                    // MEDICOS
                    case "REG_MEDICO":
                        Medico m = (Medico) paquete.getObjeto();
                        int idEspecialidad = m.getEspecialidad().getCodigoEspecialidad();
                        boolean exitoMed = medicoService.agregarMedico(m, 0, idEspecialidad);
                        paquete.setRespuesta(exitoMed);
                        break;

                    case "LISTAR_MEDICOS":
                        paquete.setRespuesta(medicoService.listarMedicos());
                        break;

                    case "BUSCAR_MEDICO":
                        String cedulaMed = (String) paquete.getObjeto();
                        paquete.setRespuesta(medicoService.buscarMedicoCedula(cedulaMed));
                        break;

                    case "UPDATE_MEDICO":
                        Medico mUpd = (Medico) paquete.getObjeto();
                        boolean exitoUpdMed = medicoService.actualizarMedico(mUpd);
                        paquete.setRespuesta(exitoUpdMed);
                        break;

                    case "DELETE_MEDICO":
                        Medico mDel = (Medico) paquete.getObjeto();
                        boolean exitoDelMed = medicoService.desactivarMedico(mDel.getCedula());
                        paquete.setRespuesta(exitoDelMed);
                        break;

                    // CLIENTES
                    case "REG_CLIENTE":
                        Cliente cli = (Cliente) paquete.getObjeto();
                        Cliente existente = clienteService.buscarClientePorCodigo(cli.getNumExpediente());
                        boolean exitoCli;
                        if (existente == null) {
                            exitoCli = clienteService.registrarNuevoCliente(cli);
                        } else {
                            exitoCli = clienteService.actualizarCliente(cli);
                        }
                        paquete.setRespuesta(exitoCli);
                        break;

                    case "LISTAR_CLIENTES":
                        paquete.setRespuesta(clienteService.getClientes());
                        break;

                    case "BUSCAR_CLIENTE":
                        String codigoCli = (String) paquete.getObjeto();
                        paquete.setRespuesta(clienteService.buscarClientePorCodigo(codigoCli));
                        break;

                    case "BUSCAR_CLIENTE_CEDULA":
                        String cedulaCli = (String) paquete.getObjeto();
                        paquete.setRespuesta(clienteService.buscarClientePorCedula(cedulaCli));
                        break;

                    case "UPDATE_CLIENTE":
                        Cliente cUpd = (Cliente) paquete.getObjeto();
                        boolean exitoUpdCli = clienteService.actualizarCliente(cUpd);
                        paquete.setRespuesta(exitoUpdCli);
                        break;

                    case "DELETE_CLIENTE":
                        String cedulaEliminar = (String) paquete.getObjeto();
                        boolean desactivado = clienteService.desactivarCliente(cedulaEliminar);
                        paquete.setRespuesta(desactivado);
                        break;

                    // ESPECIALIDADES
                    case "REG_ESPECIALIDAD":
                        Especialidad esp = (Especialidad) paquete.getObjeto();
                        boolean exitoEsp = especialidadService.registrarEspecialidad(esp);
                        paquete.setRespuesta(exitoEsp);
                        break;

                    case "LISTAR_ESPECIALIDADES":
                        paquete.setRespuesta(especialidadService.listarEspecialidades());
                        break;

                    case "BUSCAR_ESPECIALIDAD_NOMBRE":
                        String nombreEsp = (String) paquete.getObjeto();
                        paquete.setRespuesta(especialidadService.buscarEspecialidadPorNombre(nombreEsp));
                        break;

                    case "UPDATE_ESPECIALIDAD":
                        Especialidad espUpd = (Especialidad) paquete.getObjeto();
                        boolean exitoUpdEsp = especialidadService.actualizarEspecialidad(espUpd);
                        paquete.setRespuesta(exitoUpdEsp);
                        break;

                    case "DELETE_ESPECIALIDAD":
                        String codEspEliminar = (String) paquete.getObjeto();
                        boolean exitoDelEsp = especialidadService.eliminarEspecialidad(codEspEliminar);
                        paquete.setRespuesta(exitoDelEsp);
                        break;

                    // CITAS
                    case "REG_CITA":
                        Cita c = (Cita) paquete.getObjeto();
                        LocalDateTime inicioCita = c.getFechaCita();
                        LocalDateTime finCita = inicioCita.plusMinutes(30);
                        boolean disponible = medicoService.verificarDisponibilidad(c.getMedico().getCedula(), inicioCita, finCita);
                        if (disponible) {
                            boolean exitoCita = citaService.crearCita(c, c.getMedico().getCedula(), c.getCliente().getCedula());
                            paquete.setRespuesta(exitoCita);
                        } else {
                            paquete.setRespuesta(false);
                        }
                        break;

                    case "LISTAR_CITAS":
                        paquete.setRespuesta(citaService.getTodasLasCitas());
                        break;

                    case "BUSCAR_CITA":
                        String codigoCita = (String) paquete.getObjeto();
                        paquete.setRespuesta(citaService.buscarCita(Integer.parseInt(codigoCita)));
                        break;

                    case "EDIT_CITA":
                        Cita citaMod = (Cita) paquete.getObjeto();
                        LocalDateTime inicioMod = citaMod.getFechaCita();
                        LocalDateTime finMod = inicioMod.plusMinutes(30);
                        boolean disponibleMod = medicoService.verificarDisponibilidad(citaMod.getMedico().getCedula(), inicioMod, finMod);
                        if (disponibleMod) {
                            boolean exitoEditCita = citaService.editCita(citaMod.getCodigoCita(), citaMod.getFechaCita(), citaMod.getMedico().getCedula());
                            paquete.setRespuesta(exitoEditCita);
                        } else {
                            paquete.setRespuesta(false);
                        }
                        break;

                    case "CANCEL_CITA":
                        Cita cCancel = (Cita) paquete.getObjeto();
                        boolean exitoCancelCita = citaService.cancelCita(cCancel.getCodigoCita());
                        paquete.setRespuesta(exitoCancelCita);
                        break;

                    // CONSULTA
                    case "REG_CONSULTA":
                        try {
                            Consulta cons = (Consulta) paquete.getObjeto();
                            String cedMed = (cons.getMedico() != null) ? cons.getMedico().getCedula() : "";
                            String cedCli = (cons.getCliente() != null) ? cons.getCliente().getCedula() : "";

                            boolean exitoCons = consultaService.registrarConsultaCompleta(cons, cedMed, cedCli);
                            paquete.setRespuesta(exitoCons);
                        } catch (Exception e) {
                            System.out.println("Error detallado al registrar consulta:");
                            e.printStackTrace(); // <-- Esto imprimirá el error exacto en tu consola de Eclipse/IntelliJ
                            paquete.setRespuesta(false);
                        }
                        break;

                    // VACUNAS
                    case "REG_VACUNA":
                        Vacuna v = (Vacuna) paquete.getObjeto();
                        boolean exitoVac = vacunaService.agregarVacuna(v);
                        paquete.setRespuesta(exitoVac);
                        break;

                    case "LISTAR_VACUNAS":
                        paquete.setRespuesta(vacunaService.listarVacunas());
                        break;

                    case "APLICAR_VACUNA":
                        RegistroVacunacion reg = (RegistroVacunacion) paquete.getObjeto();
                        int idVacuna = reg.getLote().getVacuna().getCodigoVacuna();
                        boolean exitoApliVac = vacunaService.aplicarVacunaCliente(reg.getCliente().getCedula(), idVacuna, Timestamp.valueOf(reg.getFecha()));
                        paquete.setRespuesta(exitoApliVac);
                        break;

                    case "UPDATE_VACUNA":
                        Vacuna vacUpd = (Vacuna) paquete.getObjeto();
                        paquete.setRespuesta(vacunaService.actualizarVacuna(vacUpd));
                        break;

                    case "DELETE_VACUNA":
                        int idVac = (int) paquete.getObjeto();
                        paquete.setRespuesta(vacunaService.eliminarVacuna(idVac));
                        break;

                    // LOTEVACUNA
                    case "REG_LOTE_VACUNA":
                        LoteVacuna lote = (LoteVacuna) paquete.getObjeto();
                        paquete.setRespuesta(loteVacunaService.registrarLote(lote));
                        break;

                    case "LISTAR_LOTES_VACUNAS":
                        paquete.setRespuesta(loteVacunaService.listarLotes());
                        break;

                    case "LISTAR_LOTES_POR_VACUNA":
                        int codVac = (int) paquete.getObjeto();
                        paquete.setRespuesta(loteVacunaService.listarLotesPorVacuna(codVac));
                        break;

                    case "UPDATE_LOTE_VACUNA":
                        LoteVacuna loteUpd = (LoteVacuna) paquete.getObjeto();
                        paquete.setRespuesta(loteVacunaService.editLoteVacuna(loteUpd));
                        break;

                    case "DELETE_LOTE_VACUNA":
                        int codLoteDel = (int) paquete.getObjeto();
                        paquete.setRespuesta(loteVacunaService.eliminarLoteVacuna(codLoteDel));
                        break;

                    // ENFERMERA
                    case "REG_ENFERMERA":
                        Enfermera enfReg = (Enfermera) paquete.getObjeto();
                        paquete.setRespuesta(enfermeraService.crearEnfermera(enfReg));
                        break;

                    case "LISTAR_ENFERMERAS":
                        paquete.setRespuesta(enfermeraService.listarEnfermeras());
                        break;

                    case "UPDATE_ENFERMERA":
                        Enfermera enfUpd = (Enfermera) paquete.getObjeto();
                        paquete.setRespuesta(enfermeraService.editEnfermera(enfUpd));
                        break;

                    case "DELETE_ENFERMERA":
                        Enfermera enfDel = (Enfermera) paquete.getObjeto();
                        paquete.setRespuesta(enfermeraService.desactivarEnfermera(enfDel.getCedula()));
                        break;

                    // EVALUACION_FISICA
                    case "REG_EVALUACION_FISICA":
                        EvaluacionFisica eval = (EvaluacionFisica) paquete.getObjeto();
                        paquete.setRespuesta(evaluacionFisicaService.registrarEvaluacion(eval));
                        break;

                    // MEDICAMENTO
                    case "REG_MEDICAMENTO":
                        Medicamento medicamento = (Medicamento) paquete.getObjeto();
                        paquete.setRespuesta(medicamentoService.crearMedicamento(medicamento));
                        break;

                    case "LISTAR_MEDICAMENTOS":
                        paquete.setRespuesta(medicamentoService.listarMedicamentos());
                        break;

                    case "UPDATE_MEDICAMENTO":
                        Medicamento medUpd = (Medicamento) paquete.getObjeto();
                        paquete.setRespuesta(medicamentoService.editMedicamento(medUpd));
                        break;

                    case "DELETE_MEDICAMENTO":
                        int codMedDel = (int) paquete.getObjeto();
                        paquete.setRespuesta(medicamentoService.eliminarMedicamento(codMedDel));
                        break;

                    // RECETA_MEDICA
                    case "REG_RECETA_MEDICA":
                        RecetaMedica receta = (RecetaMedica) paquete.getObjeto();
                        paquete.setRespuesta(recetaMedicaService.crearRecetaMedica(receta));
                        break;

                    // TIPO_ANALISIS
                    case "REG_TIPO_ANALISIS":
                        TipoAnalisis tAnalisis = (TipoAnalisis) paquete.getObjeto();
                        paquete.setRespuesta(tipoAnalisisService.crearTipoAnalisis(tAnalisis));
                        break;

                    case "LISTAR_TIPOS_ANALISIS":
                        paquete.setRespuesta(tipoAnalisisService.listarTiposAnalisis());
                        break;

                    // ANALISIS
                    case "REG_ANALISIS":
                        Analisis analisis = (Analisis) paquete.getObjeto();
                        paquete.setRespuesta(analisisService.crearAnalisis(analisis));
                        break;

                    default:
                        paquete.setRespuesta(null);
                        break;
                }

                FlujoEscritura.writeObject(paquete);
                FlujoEscritura.flush();
            }
        } catch (SocketException se) {
            System.out.println("Cliente desconectado (Socket cerrado).");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en flujo de datos: " + e);
        } finally {
            try {
                if (nsfd != null) nsfd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}