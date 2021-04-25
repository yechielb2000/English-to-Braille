package com.example.test1244;

import android.widget.Toast;

public class Translate {

    private final String tempText;

    public Translate(String text) {
        this.tempText = text;
    }

    public String translateText() {

        String result = "";
        char temp1, temp2, temp3, temp4;

        try {
            for (int i = 0; i < tempText.length(); i++) {

                temp1 = tempText.charAt(i);
                if (i < tempText.length() - 1) {

                    if (i < tempText.length() - 3) {

                        temp1 = tempText.charAt(i);
                        temp2 = tempText.charAt(i + 1);
                        temp3 = tempText.charAt(i + 2);
                        temp4 = tempText.charAt(i + 3);

                        if (temp1 == 't' && temp2 == 'h' && temp3 == 'e' && temp4 == ' ') {

                            result += "⠮";
                            i += 2;
                            continue;

                        } else if (temp1 == 'f' && temp2 == 'o' && temp3 == 'r' && temp4 == ' ') {

                            result += "⠿";
                            i += 2;
                            continue;

                        } else if (temp1 == 'a' && temp2 == 'n' && temp3 == 'd' && temp4 == ' ') {

                            result += "⠯";
                            i += 2;
                            continue;

                        } else if (temp1 == 'i' && temp2 == 'n' && temp3 == 'g' && temp4 == ' ') {

                            result += "⠬";
                            i += 2;
                            continue;
                        }
                    }
                    temp2 = tempText.charAt(i + 1);

                    if (temp1 == 'c' && temp2 == 'h') {

                        result += "⠡";
                        i++;

                    } else if (temp1 == 's' && temp2 == 't') {

                        result += "⠌";
                        i++;

                    } else if (temp1 == 'g' && temp2 == 'h') {

                        result += "⠣";
                        i++;

                    } else if (temp1 == 's' && temp2 == 'h') {

                        result += "⠩";
                        i++;

                    } else if (temp1 == 't' && temp2 == 'h') {

                        result += "⠹";
                        i++;

                    } else if (temp1 == 'w' && temp2 == 'h') {

                        result += "⠱";
                        i++;

                    } else if (temp1 == 'e' && temp2 == 'd') {

                        result += "⠫";
                        i++;

                    } else if (temp1 == 'e' && temp2 == 'r') {

                        result += "⠻";
                        i++;

                    } else if (temp1 == 'a' && temp2 == 'r') {

                        result += "⠜";
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'w') {

                        result += "⠪";
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'u') {

                        result += "⠳";
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'f') {

                        result += "⠷";
                        i++;

                    } else if (temp1 == 'o' && temp2 == 'r') {

                        result += "⠶";
                        i++;

                    } else if (temp1 == 'i' && temp2 == 'n') {

                        result += "⠔";
                        i++;

                    } else {
                        temp1 = tempText.charAt(i);

                        if (32 <= temp1 && temp1 <= 47) {

                            result += BrailleTable.sign[temp1 - (int) ' '];

                        } else if (48 <= temp1 && temp1 <= 57) {

                            result += BrailleTable.number[temp1 - (int) '0'];

                        } else if (58 <= temp1 && temp1 <= 64) {

                            result += BrailleTable.sign2[temp1 - (int) ':'];

                        } else if (91 <= temp1 && temp1 <= 96) {

                            result += BrailleTable.sign3[temp1 - (int) '['];

                        } else if (97 <= temp1 && temp1 <= 122) {

                            result += BrailleTable.base[temp1 - (int) 'a'];
                        }
                    }
                }
            }
            return result;
        } catch (Exception e) {
            Toast.makeText( MainActivity.main_layout.getContext(), "Error message: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
