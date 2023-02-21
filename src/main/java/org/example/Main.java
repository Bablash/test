package org.example;


public class Main {

    public static void main(String[] args) {

        boolean flag = false;
        Array myArray = new Array();

        for(int i = 0; i < args.length; i++){
            switch(i) {
                case 0:
                    switch (args[i]) {
                        case "-a": //прямая сортировка устанавливаются при создании объекта класса в конструкторе
                            break;
                        case "-i":
                            myArray.setType("int");
                            break;
                        case "-d":
                            myArray.setMode(false);
                            break;
                        case "-s":
                            myArray.setType("string");
                            break;
                        default:
                            System.out.println("Invalid data");
                            System.exit(1);
                    }
                    break;
                case 1:
                    switch (args[i]) {
                        case "-i":
                            myArray.setType("int");
                            flag = true;
                            break;
                        case "-s":
                            myArray.setType("string");
                            flag = true;
                            break;
                        default:
                            myArray.setWriter(args[i]);
                    }
                    break;
                case 2:
                    if (flag)
                        myArray.setWriter(args[i]);
                    else myArray.setReader(args[i]);
                    break;
                default: myArray.setReader(args[i]);
            }
        }
        myArray.sort();
    }
}