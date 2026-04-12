package br.gov.saude.ubs.util;

import javax.swing.text.MaskFormatter;

public class MascaraUtil {
    public static MaskFormatter aplicar(String mascara) {
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(false); // Ajuda a pegar o valor limpo
            return mf;
        } catch (Exception e) {
            return null;
        }
    }

    // Converte DD/MM/AAAA para YYYY-MM-DD (Para o Banco)
    public static String paraISO(String dataBR) {
        if (dataBR == null || dataBR.contains("_")) return "";
        String[] partes = dataBR.split("/");
        return partes[2] + "-" + partes[1] + "-" + partes[0];
    }

    // Converte YYYY-MM-DD para DD/MM/AAAA (Para a Tela)
    public static String paraBR(String dataISO) {
        if (dataISO == null || !dataISO.contains("-")) return dataISO;
        String[] partes = dataISO.split("-");
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }
}