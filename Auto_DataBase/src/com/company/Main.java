package com.company;

import com.company.Cell;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner scn;
    static ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>();
    static ArrayList<String> titleList = new ArrayList<String>();
    static Scanner commandRead = new Scanner(System.in);
    static int carIndex;
    static ArrayList<Integer> indexData =  new ArrayList<Integer>();

    public static void main(String[] args) {
        DataInitialize();
        DataRead();
        Menu();



    }

    public static void Menu() {

        while (true) {
            System.out.println("Введите комманду:");
            System.out.println("1.Добавить автомобиль");
            System.out.println("2.Удалить автомобиль");
            System.out.println("3.Редактировать информацию об Автомобиле");
            System.out.println("4.Показать список автомобилей");
            System.out.println("0.Выход");
            int choice = commandRead.nextInt();
            try {
                switch (choice) {
                    case 1:
                        AddAuto();
                        break;
                    case 2:
                        DeleteAuto();
                        break;
                    case 3:
                        EditAuto();
                        break;
                    case 4:
                        ShowData();
                        break;
                    case 0:
                        System.exit(0);
                    default:
                        System.out.println("Неверная Команда!");
                        break;
                }
            }
            catch (Exception e) {
                System.out.println("Действие не выполнено! Возврат в меню..");
            }

        }
    }


    private static void DeleteAuto() throws Exception {
        int choosePos;
        try{
            choosePos = ChooseAuto();
        }
        catch (Exception e){
            throw e;
        }
        if (choosePos == 0) return;
        data.remove(choosePos-1);
        indexData.remove(indexData.size()-1);
        Save();
    }

    private static void EditAuto() throws Exception {
        int choosePos;
        try{
            choosePos = ChooseAuto();
        }
        catch (Exception e){
            throw e;
        }
        if (choosePos == 0) return; //продолжаем путь обратно в меню
        ArrayList<Cell> editAutoInfo = new ArrayList<Cell>();
        String editValue;
        for (String title:titleList)
        {
                System.out.println("Введите новые данные для значения: " + title);
                if (commandRead.hasNext()) {
                    editValue = commandRead.next();
                    Cell editAutoCell = new Cell();
                    editAutoCell.setTitle(title);
                    editAutoCell.setValue(editValue);
                    editAutoInfo.add(editAutoCell);
                }


        }
            data.set(choosePos-1,editAutoInfo);
        Save();
        System.out.println("Информация успешно изменена!");
    }

    private static int ChooseAuto() throws Exception {
        System.out.println("Выберите авто из списка:");
        ShowData();
        System.out.println("Для выхода в главное меню нажмите 0.");
        int pos;
        try {
            pos = commandRead.nextInt();
            if (pos == 0) return 0;  //возвращаемся в меню
            if (pos > indexData.size()) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Некорректное значение для выбора авто!");
            throw e;
        }
        return pos;
    }

    private static void ShowData() {

        for (int index = 0; index < indexData.size(); index++)
        {
            System.out.print(indexData.get(index) + ". ");
            for (Cell dataCell : data.get(index)) //вывод данных по автомобилю в консоль
            {
                System.out.print(dataCell.getValue()+' ');
            }
            System.out.println();
        }
    }


    private static void AddAuto() { //complete
        ArrayList<Cell> newAutoInfo = new ArrayList<Cell>();
        String newValue;
        for (String title:titleList)
        {
            System.out.println("Укажите "+title);
            if (commandRead.hasNext()) {
                newValue = commandRead.next();
                Cell newAutoCell = new Cell();
                newAutoCell.setTitle(title);
                newAutoCell.setValue(newValue);
                newAutoInfo.add(newAutoCell);
            }
        }
        data.add(newAutoInfo);
        indexData.add(indexData.size()+1);
        Save();
    }

    private static void Save() {
        try(FileWriter writer = new FileWriter("src\\Data.txt", false))
        {
            for (int i = 0; i<titleList.size()-1; i++)
            {
                writer.write(titleList.get(i)+' ');
            }
            writer.write(titleList.get(titleList.size()-1)+'\n'); //чтобы файл не заполнялся пробелами(?)

            for (int rowIndex = 0; rowIndex < data.size()-1; rowIndex++)
            {
                ArrayList<Cell> row = new ArrayList<>(data.get(rowIndex));
                for (int i = 0; i<row.size()-1; i++)
                {
                    writer.write(row.get(i).getValue()+' ');
                }
                writer.write(row.get(row.size()-1).getValue()+'\n'); //каждый конец строчки также записываем без пробела
            }

            ArrayList<Cell> finalrow = new ArrayList<>(data.get(data.size()-1)); //последняя строка выполнена отдельно, чтобы файл не заполнялся пропусками строки(?)
            for (int i = 0; i<finalrow.size()-1; i++)
            {
                writer.write(finalrow.get(i).getValue()+' ');
            }
            writer.write(finalrow.get(finalrow.size()-1).getValue()); //по аналогичным причинам отдельно прочтен последний элемент
        }
        catch(Exception ex){
            System.out.println("Ошибка при сохранении!");
        }

    }



    private static void DataRead() {  //на выходе получаем заполненные data и indexData
        String s = scn.nextLine();
        Scanner row_scn = new Scanner (s);
        while(row_scn.hasNext()) {
            titleList.add(row_scn.next());//прочли и добавили заголовок
        }

        carIndex = 1;
        while (scn.hasNextLine()) { //считываем инфо по автомобилям и заполнеяем базу
            ArrayList<Cell> carData = new ArrayList<Cell>();
            s = scn.nextLine();
            row_scn = new Scanner (s);

            indexData.add(carIndex);
            int i = 0;
            while(row_scn.hasNext()) {  //сканируем поэлементно строку в carData
                Cell cell = new Cell();
                cell.setTitle(titleList.get(i));
                cell.setValue(row_scn.next());
                carData.add(cell);
                i++;
            }
            data.add(carData); //заносим строку в базу
            carIndex++;
        }
        System.out.println("База данных загружена.");
        row_scn.close();
        scn.close();
    }



    private static void DataInitialize() {
        try{
        scn = new Scanner (new File("src\\Data.txt"));
//            System.out.println("Файл найден");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Файл не найден!");
            System.exit(0);
        }
    }


}

