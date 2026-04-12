package br.gov.saude.ubs.util;

import javax.swing.text.MaskFormatter;

public class MascaraUtil {
    public static MaskFormatter aplicar(String mascara) {
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setPlaceholderCharacter('_');
            return mf;
        } catch (Exception e) {
            return null;
        }
    }
}