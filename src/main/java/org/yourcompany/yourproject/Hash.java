package org.yourcompany.yourproject;

import java.io.IOException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hash {
	public static void main(String[] args) throws Exception {
		String mode = null;
		String input = null;
		String expectedHash = null;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--mode":
					if (i + 1 < args.length)
						mode = args[++i];
					break;
				case "--input":
					if (i + 1 < args.length)
						input = args[++i];
					break;
				case "--hash":
					if (i + 1 < args.length)
						expectedHash = args[++i];
					break;
				default:
					System.err.println("Paràmetre desconegut: " + args[i]);
			}
		}

		if (mode == null || input == null || expectedHash == null) {
			System.err.println("Ús incorrecte.");
			System.err.println("Exemple: java -jar VerifyHash.jar --mode <text|file> --input <dada> --hash <hash>");
			System.exit(1);
		}
		if (!expectedHash.matches("[0-9a-fA-F]+") || expectedHash.length() != 64) {
			System.err.println("Hash esperat no vàlid: " + expectedHash);
			System.exit(1);
		}
		if (!mode.equalsIgnoreCase("text") && !mode.equalsIgnoreCase("file")) {
			System.err.println("Mode no vàlid: " + mode + " (usa 'text' o 'file')");
			System.exit(1);
		}
		if (mode.equalsIgnoreCase("text")) {
			System.out.println("Processant text directe: " + input);
			byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
			if (MessageDigest.isEqual(hashBytes, hexToBytes(expectedHash))) {
				System.out.println("Hash coincideix!");
			} else {
				System.out.println("Hash no coincideix!");
				System.out.println("Hash generat: " + Hexadecimal(hashBytes));
			}
		} else if (mode.equalsIgnoreCase("file")) {
			System.out.println("Llegint el fitxer a la ruta: " + input);
			Path ruta = Paths.get(input);
			if (!java.nio.file.Files.exists(ruta)) {
				System.err.println("El fitxer no existeix: " + input);
				System.exit(1);
			}
			try {
				byte[] fileBytes = java.nio.file.Files.readAllBytes(ruta);
				byte[] hashBytes = md.digest(fileBytes);
				if (MessageDigest.isEqual(hashBytes, hexToBytes(expectedHash))) {
					System.out.println("Hash coincideix!");
				} else {
					System.out.println("Hash no coincideix!");
					System.out.println("Hash generat: " + Hexadecimal(hashBytes));
				}
			} catch (IOException e) {
				System.err.println("Error llegint el fitxer: " + e.getMessage());
				System.exit(1);
			}
		} else {
			System.err.println("Mode no vàlid: " + mode + " (usa 'text' o 'file')");
		}
	}
		private static String Hexadecimal(byte[] resumen) {
		String hex = "";
		for (int i = 0; i < resumen.length; i++)  {
			String h = Integer.toHexString(resumen[i]& 0xFF);
			if (h.length() == 1) hex += "0";
			hex += h;
		}
		return hex.toUpperCase();
	}

	private static byte[] hexToBytes(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
					+ Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

}
