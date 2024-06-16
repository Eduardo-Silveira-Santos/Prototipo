package com.eduardosantos.prototipo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberFormattingTextWatcher implements TextWatcher {

    private final EditText editText;
    private boolean isUpdating;

    public PhoneNumberFormattingTextWatcher(EditText editText) {
        this.editText = editText;
        this.isUpdating = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) {
            return;
        }

        isUpdating = true;

        // Remove caracteres não numéricos
        String phone = s.toString().replaceAll("[^\\d]", "");

        // Aplica a máscara
        StringBuilder formattedPhone = new StringBuilder();
        int length = phone.length();

        if (length > 0) {
            formattedPhone.append("(");
        }
        if (length > 2) {
            formattedPhone.append(phone.substring(0, 2)).append(") ");
            phone = phone.substring(2);
            length = phone.length();
        }
        if (length > 5) {
            formattedPhone.append(phone.substring(0, 5)).append("-");
            phone = phone.substring(5);
            length = phone.length();
        }
        if (length > 0) {
            formattedPhone.append(phone);
        }

        // Atualiza o texto do EditText
        editText.setText(formattedPhone.toString());
        editText.setSelection(formattedPhone.length());

        isUpdating = false;
    }
}
