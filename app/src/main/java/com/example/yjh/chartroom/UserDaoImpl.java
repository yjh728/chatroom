package com.example.yjh.chartroom;

import java.io.*;

/**
 * 这是用户操作的具体实现类(IO版)
 *
 * @author yjh
 * @version V1.1
 */
public class UserDaoImpl implements UserDao {
    private static File file = new File("user.txt");

    static {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean islogin(String username, String password) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new FileReader(file));
            String s = new String();
            while ((s = bufferedReader.readLine()) != null) {
                String[] strings = s.split("=");
                if (strings[0].equals(username) && strings[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean regist(User user) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedReader = new BufferedReader(
                    new FileReader(file));
            char[] bytes = new char[1024];
            int len = 0;
            while ((len = bufferedReader.read(bytes)) != -1) {
                String[] strings = new String(bytes, 0, len).split("=");
                if (strings[0].equals(user.getUsername())) {
                    return false;
                }
            }
            bufferedWriter.write(user.getUsername() + "=" + user.getPassword());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
