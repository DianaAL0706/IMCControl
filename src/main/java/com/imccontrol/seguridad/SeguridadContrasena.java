
package com.imccontrol.seguridad;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SeguridadContrasena {

    private static final int ITERACIONES = 600000;
    private static final int LONGITUD_SAL = 16;
    private static final int LONGITUD_HASH = 256;
    private static final String ALGORITMO =
            "PBKDF2WithHmacSHA256";

    public static String generarHash(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía.");
        }

        byte[] sal = new byte[LONGITUD_SAL];
        new SecureRandom().nextBytes(sal);

        byte[] hash = calcularHash(
                contrasena, sal, ITERACIONES);

        return ITERACIONES + ":"
                + Base64.getEncoder().encodeToString(sal)
                + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificarContrasena(
            String contrasena, String hashGuardado) {

        if (contrasena == null || hashGuardado == null) {
            return false;
        }

        try {
            String[] partes = hashGuardado.split(":");

            if (partes.length != 3) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[0]);

           if (iteraciones < 1 || iteraciones > ITERACIONES) {
                 return false;
           }
           
            byte[] sal = Base64.getDecoder()
                    .decode(partes[1]);

            byte[] hashOriginal = Base64.getDecoder()
                    .decode(partes[2]);

            if (sal.length != LONGITUD_SAL
                    || hashOriginal.length != LONGITUD_HASH / 8) {
                return false;
            }

            byte[] hashCalculado = calcularHash(
                    contrasena, sal, iteraciones);

            return MessageDigest.isEqual(
                    hashOriginal, hashCalculado);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] calcularHash(
            String contrasena, byte[] sal, int iteraciones) {

        PBEKeySpec especificacion = new PBEKeySpec(
                contrasena.toCharArray(),
                sal,
                iteraciones,
                LONGITUD_HASH);

        try {
            SecretKeyFactory fabrica =
                    SecretKeyFactory.getInstance(ALGORITMO);

            return fabrica.generateSecret(
                    especificacion).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "No se pudo procesar la contraseña.", e);
        } finally {
            especificacion.clearPassword();
        }
    }
}
