package helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permissao {
    public static boolean validarPermissoes(String [] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listPermissoes = new ArrayList<>();

            for(String permissao : permissoes){

                boolean truePermission = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!truePermission) listPermissoes.add(permissao);
                if(listPermissoes.isEmpty()) return true;

            }
            String[] novasPermissoes = new String[listPermissoes.size()];
            listPermissoes.toArray(novasPermissoes);
            //SOlicita permissões
            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode);
        }

        return true;
    }
}
