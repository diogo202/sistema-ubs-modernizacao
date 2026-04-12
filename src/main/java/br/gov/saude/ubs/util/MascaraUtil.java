package br.gov.saude.ubs.util;

import javax.swing.text.MaskFormatter;

public class MascaraUtil {
    
    public static MaskFormatter aplicar(String mascara) {
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(false); 
            return mf;
        } catch (Exception e) {
            return null;
        }
    }

    // Máscaras Úteis
    public static final String MASCARA_CPF = "###.###.###-##";
    public static final String MASCARA_CNS = "###############"; // 15 dígitos
    public static final String MASCARA_DATA = "##/##/####";
    public static final String MASCARA_CEP = "#####-###";
    public static final String MASCARA_CELULAR = "(##) #####-####";
    public static final String MASCARA_FIXO = "(##) ####-####";

    public static String paraISO(String dataBR) {
        if (dataBR == null || dataBR.trim().length() < 10) return "";
        try {
            String[] partes = dataBR.split("/");
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        } catch (Exception e) { return ""; }
    }
}