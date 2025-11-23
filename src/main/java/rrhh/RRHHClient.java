package rrhh;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RRHHClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n Gestión RRHH (Cliente enJava)");
            System.out.println("1. Insertar Empleado");
            System.out.println("2. Actualizar Empleado");
            System.out.println("3. Consultar Empleado");
            System.out.println("4. Borrar Empleado (Genera Histórico)");
            System.out.println("5. Salir");
            System.out.print("Seleccione opción: ");

            String opcion = scanner.nextLine();
            if (opcion.equals("5")) break;

            String mensajeEnviar = "";

            try {
                switch (opcion) {
                    case "1": // Insert
                        mensajeEnviar = construirMensajeInsert(scanner);
                        break;
                    case "2": // Update
                        mensajeEnviar = construirMensajeUpdate(scanner);
                        break;
                    case "3": // Select
                        System.out.print("Ingrese ID a consultar: ");
                        mensajeEnviar = "SELECT;" + scanner.nextLine();
                        break;
                    case "4": // Delete
                        System.out.print("Ingrese ID a borrar: ");
                        mensajeEnviar = "DELETE;" + scanner.nextLine();
                        break;
                    default:
                        System.out.println("Opción incorrecta.");
                        continue;
                }

                // Comunicación con el Servidor
                enviarPeticion(mensajeEnviar);

            } catch (Exception e) {
                System.out.println("Error en datos: " + e.getMessage());
            }
        }
    }

    private static void enviarPeticion(String mensaje) {
        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Enviar
            out.writeUTF(mensaje);

            // Recibir respuesta y visualizar
            String respuesta = in.readUTF();
            System.out.println("\n[Respuesta del servidor]: " + respuesta);

        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }

    private static String construirMensajeInsert(Scanner sc) {

        System.out.print("Nombre: ");String nom = sc.nextLine();
        System.out.print("Apellido: ");String ape = sc.nextLine();
        System.out.print("Email: ");String mail = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");String fecha = sc.nextLine();
        System.out.print("Sueldo: ");String sueldo = sc.nextLine();
        System.out.print("Comisión: ");String comision = sc.nextLine();
        System.out.print("ID Cargo (FK): ");String cargo = sc.nextLine();
        System.out.print("ID Gerente (o 'null' si no tiene): ");String gerente = sc.nextLine();
        System.out.print("ID Departamento (FK): ");String depto = sc.nextLine();

        // Formato: INSERT;nom;ape;mail;fecha;sueldo;comision;cargo;gerente;depto
        return "INSERT;" + nom + ";" + ape + ";" + mail + ";" + fecha + ";" +
                sueldo + ";" + comision + ";" + cargo + ";" + gerente + ";" + depto;
    }

    private static String construirMensajeUpdate(Scanner sc) {

        System.out.print("ID Empleado a Modificar: ");String id = sc.nextLine();
        System.out.print("Nuevo Nombre: ");String nom = sc.nextLine();
        System.out.print("Nuevo Apellido: ");String ape = sc.nextLine();
        System.out.print("Nuevo Email: "); String mail = sc.nextLine();
        System.out.print("Nueva Fecha de nacimiento (YYYY-MM-DD): "); String fecha = sc.nextLine();
        System.out.print("Nuevo Sueldo: ");String sueldo = sc.nextLine();
        System.out.print("Nueva Comisión: "); String comision = sc.nextLine();
        System.out.print("Nuevo ID Cargo (FK): "); String cargo = sc.nextLine();
        System.out.print("Nuevo ID Gerente (o 'null'): "); String gerente = sc.nextLine();
        System.out.print("Nuevo ID Depto (FK): ");String depto = sc.nextLine();

        // Formato final compatible con UPDATE
        return "UPDATE;" + id + ";" + nom + ";" + ape + ";" + mail + ";" + fecha + ";" +
                sueldo + ";" + comision + ";" + cargo + ";" + gerente + ";" + depto;
    }



}
