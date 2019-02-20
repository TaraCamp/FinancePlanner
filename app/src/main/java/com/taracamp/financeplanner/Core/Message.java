package com.taracamp.financeplanner.Core;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Message {

    public static void show(Context _context, String _message, Message.Mode _state) {
        if (_state.equals(Mode.ERROR))
        {
            Toasty.error(_context, _message, Toast.LENGTH_SHORT, true).show();
        }
        else if (_state.equals(Mode.SUCCESS))
        {
            Toasty.success(_context, _message, Toast.LENGTH_SHORT, true).show();
        }
        else if (_state.equals(Mode.INFO))
        {
            Toasty.info(_context, _message, Toast.LENGTH_SHORT, true).show();
        }
        else if (_state.equals(Mode.WARNING))
        {
            Toasty.warning(_context, _message, Toast.LENGTH_SHORT, true).show();
        }
    }

    public enum Mode {
        ERROR,
        SUCCESS,
        INFO,
        WARNING
    }
}
