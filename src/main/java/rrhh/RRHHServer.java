package rrhh;

import java.io.*;
import java.net.*;
import java.sql.*;

public class RRHHServer {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_rh_poligran";
    private static final String USER = "root";
    private static final String PASS = "1234";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor RRHHH iniciado en el puerto " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Esto es una clase interna pa manejar cada cliente en un hilo separado
    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    Connection con = DriverManager.getConnection(DB_URL, USER, PASS)
            ) {
                // Leer el mensaje del cliente (formato esperado: "ACCION;DATO1;DATO2...")
                String mensajeCliente = in.readUTF();
                String[] partes = mensajeCliente.split(";");
                String accion = partes[0];
                String respuesta = "Error desconocido";

                System.out.println("Recibido: " + mensajeCliente);

                switch (accion) {
                    case "INSERT":
                        respuesta = insertarEmpleado(con, partes);
                        break;
                    case "UPDATE":
                        respuesta = actualizarEmpleado(con, partes);
                        break;
                    case "SELECT":
                        respuesta = consultarEmpleado(con, partes[1]);
                        break;
                    case "DELETE":
                        respuesta = borrarEmpleadoTransaccional(con, partes[1]);
                        break;
                    default:
                        respuesta = "Acción no válida";
                }

                out.writeUTF(respuesta);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Insertar un empleado
        private String insertarEmpleado(Connection con, String[] datos) {
            String sql = "INSERT INTO EMPLEADOS (" +
                    "empl_nombre, empl_apellido, empl_email, empl_fecha_nac, " +
                    "empl_sueldo, empl_comision, empl_cargo_ID, empl_gerente_ID, empl_dpto_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, datos[1]); // Nombre
                pstmt.setString(2, datos[2]); // Apellido
                pstmt.setString(3, datos[3]); // Email
                pstmt.setString(4, datos[4]); // Fecha nac (YYYY-MM-DD)
                pstmt.setDouble(5, Double.parseDouble(datos[5])); // Sueldo
                pstmt.setDouble(6, Double.parseDouble(datos[6])); // Comisión
                pstmt.setInt(7, Integer.parseInt(datos[7])); // Cargo (FK)

                // el gerente puede ser NULL
                if (datos[8].equalsIgnoreCase("null") || datos[8].isEmpty()) {
                    pstmt.setNull(8, java.sql.Types.INTEGER);
                } else {
                    pstmt.setInt(8, Integer.parseInt(datos[8]));
                }

                pstmt.setInt(9, Integer.parseInt(datos[9])); // Departamento (FK)

                pstmt.executeUpdate();
                return "Exitoso: Empleado insertado correctamente.";
            } catch (SQLException e) {
                return "Error SQL: " + e.getMessage();
            }
        }

        // Actualizar un empleado
        private String actualizarEmpleado(Connection con, String[] datos) {
            String sql = "UPDATE EMPLEADOS SET "
                    + "empl_nombre = ?, "
                    + "empl_apellido = ?, "
                    + "empl_email = ?, "
                    + "empl_fecha_nac = ?, "
                    + "empl_sueldo = ?, "
                    + "empl_comision = ?, "
                    + "empl_cargo_ID = ?, "
                    + "empl_gerente_ID = ?, "
                    + "empl_dpto_ID = ? "
                    + "WHERE empl_ID = ?";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, datos[2]);  // nombre
                pstmt.setString(2, datos[3]);  // apellido
                pstmt.setString(3, datos[4]);  // email
                pstmt.setString(4, datos[5]);  // fecha nacimiento YYYY-MM-DD
                pstmt.setDouble(5, Double.parseDouble(datos[6])); // sueldo
                pstmt.setDouble(6, Double.parseDouble(datos[7])); // comisión
                pstmt.setInt(7, Integer.parseInt(datos[8])); // cargo ID

                // gerente puede ser NULL
                if (datos[9].equalsIgnoreCase("null") || datos[9].isEmpty()) {
                    pstmt.setNull(8, java.sql.Types.INTEGER);
                } else {
                    pstmt.setInt(8, Integer.parseInt(datos[9]));
                }

                pstmt.setInt(9, Integer.parseInt(datos[10])); // dpto ID
                pstmt.setInt(10, Integer.parseInt(datos[1])); // ID empleado (esta es la parte del WHERE)
                int filas = pstmt.executeUpdate();
                return filas > 0 ? "Éxito: Empleado actualizado." : "Error: ID no encontrado.";

            } catch (SQLException e) {
                return "Error SQL: " + e.getMessage();
            }
        }

        // Consultar un empleado
        private String consultarEmpleado(Connection con, String idStr) {
            String sql = "SELECT empl_ID, empl_nombre, empl_apellido, empl_email, empl_fecha_nac, " +
                    "empl_sueldo, empl_comision, empl_cargo_ID, empl_gerente_ID, empl_dpto_ID " +
                    "FROM EMPLEADOS WHERE empl_ID = ?";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                pstmt.setInt(1, Integer.parseInt(idStr));
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {

                    String gerente = rs.getString("empl_gerente_ID");
                    if (rs.wasNull()) gerente = "Sin gerente";

                    return String.format(
                            "ID: %d\nNombre: %s\nApellido: %s\nEmail: %s\nFecha Nac: %s\n" +
                                    "Sueldo: %.2f\nComisión: %.2f\nCargo ID: %d\nGerente ID: %s\nDepartamento ID: %d",
                            rs.getInt("empl_ID"),
                            rs.getString("empl_nombre"),
                            rs.getString("empl_apellido"),
                            rs.getString("empl_email"),
                            rs.getString("empl_fecha_nac"),
                            rs.getDouble("empl_sueldo"),
                            rs.getDouble("empl_comision"),
                            rs.getInt("empl_cargo_ID"),
                            gerente,
                            rs.getInt("empl_dpto_ID")
                    );

                } else {
                    return "No se encontró un empleado con ese ID.";
                }

            } catch (SQLException e) {
                return "Error SQL: " + e.getMessage();
            }
        }

        // d. Borrar empleado pero con transacción, o sea histórico
        private String borrarEmpleadoTransaccional(Connection con, String idStr) {
            PreparedStatement selectStmt = null;
            PreparedStatement insertHistStmt = null;
            PreparedStatement deleteStmt = null;
            int id = Integer.parseInt(idStr);

            try {
                // 1. Iniciar Transacción (Desactivar AutoCommit)
                con.setAutoCommit(false);

                // 2. Obtener datos actuales para mover al histórico
                String sqlSelect = "SELECT empl_cargo_ID, empl_dpto_ID FROM EMPLEADOS WHERE empl_ID = ?";
                selectStmt = con.prepareStatement(sqlSelect);
                selectStmt.setInt(1, id);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    return "Error: El empleado no existe, no se puede borrar.";
                }

                int cargoId = rs.getInt("empl_cargo_ID");
                int dptoId = rs.getInt("empl_dpto_ID");

                // 3. Insertar en tabla HISTORICO
                // Nota: emphist_fecha_retiro se llena con la fecha actual del sistema
                String sqlHist = "INSERT INTO HISTORICO (emphist_ID, emphist_fecha_retiro, emphist_cargo_ID, emphist_dpto_ID) VALUES (?, ?, ?, ?)";
                insertHistStmt = con.prepareStatement(sqlHist);
                insertHistStmt.setInt(1, id); // Usamos el mismo ID o generamos uno nuevo
                insertHistStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                insertHistStmt.setInt(3, cargoId);
                insertHistStmt.setInt(4, dptoId);
                insertHistStmt.executeUpdate();

                // 4. Borrar de la tabla EMPLEADOS
                String sqlDel = "DELETE FROM EMPLEADOS WHERE empl_ID = ?";
                deleteStmt = con.prepareStatement(sqlDel);
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();

                // 5. Confirmar Transacción
                con.commit();
                return "Transacción Completa: Empleado movido al histórico y eliminado.";

            } catch (SQLException e) {
                try {
                    System.out.println("Haciendo Rollback...");
                    con.rollback(); // Deshacer cambios si algo falla
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return "Error en Transacción (Rollback ejecutado): " + e.getMessage();
            } finally {
                try {
                    con.setAutoCommit(true); // Restaurar estado por defecto
                    if(selectStmt != null) selectStmt.close();
                    if(insertHistStmt != null) insertHistStmt.close();
                    if(deleteStmt != null) deleteStmt.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
