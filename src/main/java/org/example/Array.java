package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Array {
    private boolean mode;
    private ArrayList<BufferedReader> reader;

    private ArrayList<FileWriter> writer;

    private ArrayList<String> array1;
    private ArrayList<String> array2;
    private String type;
    private int count;
    Array(){
        type = "";
        count = 10; //количество элементов, которое будет считываться за раз с файла и использоваться в сортировке
        mode = true;
        reader = new ArrayList<>();
        writer = new ArrayList<>();
        array1 = new ArrayList<>();
        array2 = new ArrayList<>();
    }

    public void setMode(boolean f){
        mode = f;
    }

    public void setType(String s){
        type = s;
    }
    public void setCount(int n){
        count = n;
    }

    public void setReader(String files) {
        try {
            reader.add(new BufferedReader(new FileReader(files)));
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public void setWriter(String files) {
        try {
            writer.add(new FileWriter(files));
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    public void sort() {

        try {
            if (writer.size() == 0 && reader.size() == 0) {
                System.out.println("Не задан выходной/входной файл");
                return;
            }

            if (reader.size() == 2) { //если всего два файла на вход, то сразу записываем в выходной файл, переданный в аргументной строке
                merge_sort(reader.get(0), reader.get(1), writer.get(0));
            } else {
                setWriter("tmp1.txt");
                merge_sort(reader.get(0), reader.get(1), writer.get(writer.size() - 1));
                writer.get(writer.size() - 1).close();
                writer.remove(writer.size() - 1);
            }

            int size = reader.size();
            for (int i = 2; i < size; i++) {

                if (i % 2 == 0) { //на четных итерациях результат предыдущей сортировки лежит в tmp1.txt
                    setReader("tmp1.txt");
                    if (i == size - 1) { //если дошли до последнего входного файла
                        merge_sort(reader.get(i), reader.get(reader.size() - 1), writer.get(0)); //то результат сортировки запишем в выходной файл, переданный в аргументной строке
                        break;
                    } else
                        setWriter("tmp2.txt");
                    merge_sort(reader.get(i), reader.get(reader.size() - 1), writer.get(writer.size() - 1)); //сортируем входной файл с результатом предыдущей сортировки
                    writer.get(writer.size() - 1).close();
                    writer.remove(writer.size() - 1); //удаляем tmp из файлов на считывание
                } else {
                    setReader("tmp2.txt");//на нечетных итерациях результат предыдущей сортировки лежит в tmp2.txt
                    if (i == size - 1) {
                        merge_sort(reader.get(i), reader.get(reader.size() - 1), writer.get(0));
                        break;
                    } else
                        setWriter("tmp1.txt");
                    merge_sort(reader.get(i), reader.get(reader.size() - 1), writer.get(writer.size() - 1));
                    writer.get(writer.size() - 1).close();
                    writer.remove(writer.size() - 1);
                }
            }

            for (FileWriter fileWriter : writer) fileWriter.close();
            for (BufferedReader bufferedReader : reader) bufferedReader.close();

            Files.deleteIfExists(Paths.get("tmp1.txt"));
            Files.deleteIfExists(Paths.get("tmp2.txt"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void output(String elem, Writer file) {
        try {
            file.write(elem + "\n");
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> input(BufferedReader file, String str) {

        ArrayList<String> myArray = new ArrayList<>();

        for (int k = 0; k < count; k++) {
            String s;
            try {
                if ((s = file.readLine()) != null) {

                    if (s.contains(" ")) //если строка содержит пробел
                        continue;
                    if (type == "int") { //если сортируем int и считали не int с файла
                        try {
                            parseInt(s);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }

                    if (myArray.size() != 0)
                        if ((!(compare(s, myArray.get(myArray.size() - 1))) && !mode) || ((compare(s, myArray.get(myArray.size() - 1))) && mode))
                        //если попалось значение меньше предыдущего (прямая) или больше предыдущего (обратная сортировка)
                            continue;
                    if ( myArray.size() == 0 && str != null)
                        if((!(compare(s, str)) && !mode) || ((compare(s, str)) && mode))
                        //если попалось значение меньше последнего предыдущего массива (прямая) или больше предыдущего (обратная)
                            continue;

                    myArray.add(s);
                }
                else {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return myArray;
    }

    public boolean compare(String str1, String str2){
        if (type == "int") {
            if (parseInt(str1) < parseInt(str2))
               return true;
        }
        if (type == "string") {
            if (str1.compareTo(str2) < 0)
                return true;
        }
        return false;
    }
    public void merge_sort(BufferedReader in_file1, BufferedReader in_file2, Writer out_file) {

        boolean compare;
        String str1 = null;
        String str2 = null;

        while(true) {

            if (array1.size() == 0) {
                array1.addAll(input(in_file1, str1));
            }
            if (array2.size() == 0) {
                array2.addAll(input(in_file2, str2));
            }

            if(array1.size() != 0 && array2.size() != 0) {
                str1 = array1.get(array1.size() - 1); //запоминаем последний элемент массивов, чтобы при считывании сравнить элемент со следующим (проверка на то, что файл отсортирован)
                str2 = array2.get(array2.size() - 1);
                compare = compare(array1.get(0), array2.get(0));

                if ((!(compare) && !mode) || ((compare) && mode)) { //логическая эквивалентность, чтобы учитывать тип сортировки и результат сравнения
                    output(array1.get(0), out_file);
                    array1.remove(0);
                } else {
                    output(array2.get(0), out_file);
                    array2.remove(0);
                }
            } else if (array1.size() == 0 && array2.size() == 0) {
                break;
            }
            else if(array1.size() == 0) { //если один из массивов кончился, то второй просто записываем в конец
                output(array2.get(0), out_file);
                array2.remove(0);
            }
            else if(array2.size() == 0) {
                output(array1.get(0), out_file);
                array1.remove(0);
            }
        }
    }

}
