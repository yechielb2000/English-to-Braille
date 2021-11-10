package com.example.test1244;

import android.content.Context;
import android.widget.Toast;

public class Translator {

    private final BrailleTable brailleTable = new BrailleTable();
    private final Context context;
    private final String text;

    public Translator(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    public String translate() {
        StringBuilder result = new StringBuilder();
        char temp1, temp2, temp3, temp4;

        try {
            for (int i = 0; i < text.length(); i++) {

                temp1 = text.charAt(i);
                if (i < text.length() - 1) {

                    if (i < text.length() - 3) {

                        temp1 = text.charAt(i);
                        temp2 = text.charAt(i + 1);
                        temp3 = text.charAt(i + 2);
                        temp4 = text.charAt(i + 3);

                        if (temp1 == 't' && temp2 == 'h' && temp3 == 'e' && temp4 == ' ') {

                            result.append("⠮");
                            i += 2;
                            continue;

                        } else if (temp1 == 'f' && temp2 == 'o' && temp3 == 'r' && temp4 == ' ') {

                            result.append("⠿");
                            i += 2;
                            continue;

                        } else if (temp1 == 'a' && temp2 == 'n' && temp3 == 'd' && temp4 == ' ') {

                            result.append("⠯");
                            i += 2;
                            continue;

                        } else if (temp1 == 'i' && temp2 == 'n' && temp3 == 'g' && temp4 == ' ') {

                            result.append("⠬");
                            i += 2;
                            continue;
                        }
                    }
                    temp2 = text.charAt(i + 1);

                    if (temp1 == 'c' && temp2 == 'h') {

                        result.append("⠡");
                        i++;

                    } else if (temp1 == 's' && temp2 == 't') {

                        result.append("⠌");
                        i++;

                    } else if (temp1 == 'g' && temp2 == 'h') {

                        result.append("⠣");
                        i++;

                    } else if (temp1 == 's' && temp2 == 'h') {

                        result.append("⠩");
                        i++;

                    } else if (temp1 == 't' && temp2 == 'h') {

                        result.append("⠹");
                        i++;

                    } else if (temp1 == 'w' && temp2 == 'h') {

                        result.append("⠱");
                        i++;

                    } else if (temp1 == 'e' && temp2 == 'd') {

                        result.append("⠫");
                        i++;

                    } else if (temp1 == 'e' && temp2 == 'r') {

                        result.append("⠻");
                        i++;

                    } else if (temp1 == 'a' && temp2 == 'r') {

                        result.append("⠜");
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'w') {

                        result.append("⠪");
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'u') {

                        result.append("⠳");
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'f') {

                        result.append("⠷");
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'r') {

                        result.append("⠶");
                        i++;

                    } else if (temp1 == 'i' && temp2 == 'n') {

                        result.append("⠔");
                        i++;

                    } else {
                        temp1 = text.charAt(i);

                        if (32 <= temp1 && temp1 <= 47) {

                            result.append(brailleTable.sign[temp1 - (int) ' ']);

                        } else if (48 <= temp1 && temp1 <= 57) {

                            result.append(brailleTable.number[temp1 - (int) '0']);

                        } else if (58 <= temp1 && temp1 <= 64) {

                            result.append(brailleTable.sign2[temp1 - (int) ':']);

                        } else if (91 <= temp1 && temp1 <= 96) {

                            result.append(brailleTable.sign3[temp1 - (int) '[']);

                        } else if (97 <= temp1 && temp1 <= 122) {

                            result.append(brailleTable.base[temp1 - (int) 'a']);
                        }
                    }
                }
            }
            return result.toString();
        } catch (Exception e) {
            Toast.makeText(context, "Error message: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
