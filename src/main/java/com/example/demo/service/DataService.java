package com.example.demo.service;

import com.example.demo.model.Data;
import com.example.demo.model.HistoryItem;
import com.example.demo.model.Microcontroller;
import com.example.demo.model.RoomHistoryDto;
import com.example.demo.repository.DataRepository;
import com.example.demo.repository.MCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class DataService {
    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MCRepository mcRepository;

    public List<Data> listAllData() {
        return dataRepository.findAll();
    }

    public void saveData(Data data) {
        dataRepository.save(data);
    }

    public Data getData(Integer id) {
        return dataRepository.findById(id).get();
    }

    public void deleteData(Integer id) {
        dataRepository.deleteById(id);
    }

    public RoomHistoryDto listByMCID(int mcID) {
        Microcontroller mc = mcRepository.findById(mcID).get();

        List<Data> dataList = dataRepository.findByMCID(mc);

        return buildRoomHistoryDto(dataList);
    }

    public RoomHistoryDto listByMCIDTime(int mcID, String startStr, String endStr) {
        Microcontroller mc = mcRepository.findById(mcID).get();
        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("yyyy-MM-dd").parse(startStr);
            end = new SimpleDateFormat("yyyy-MM-dd").parse(endStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(end);
            cal.add(Calendar.HOUR_OF_DAY, 23);
            cal.add(Calendar.MINUTE, 59);
            cal.add(Calendar.SECOND, 59);
            cal.add(Calendar.DATE, 1);
            end = cal.getTime();
        } catch (ParseException e) {
            System.out.println(e);
        }


        List<Data> dataList = dataRepository.findByMCIDTime(mc, start, end);
        return buildRoomHistoryDto(dataList);
    }

    public RoomHistoryDto buildRoomHistoryDto(List<Data> dataList) {
        List<HistoryItem> temperatureHistory = new ArrayList<>();
        List<HistoryItem> humidityHistory = new ArrayList<>();
        List<HistoryItem> co2History = new ArrayList<>();
        List<HistoryItem> lightHistory = new ArrayList<>();

        for (Data data : dataList) {
            temperatureHistory.add(new HistoryItem(data.getDate_time().getTime(), String.valueOf(data.getTemperature())));
            humidityHistory.add(new HistoryItem(data.getDate_time().getTime(), String.valueOf(data.getHumidity())));
            co2History.add(new HistoryItem(data.getDate_time().getTime(), String.valueOf(data.getCo2())));
            lightHistory.add(new HistoryItem(data.getDate_time().getTime(), String.valueOf(data.getLight())));
        }
        RoomHistoryDto roomHistoryDto = new RoomHistoryDto(temperatureHistory, humidityHistory, co2History, lightHistory);
        return roomHistoryDto;
    }

    public String buildReport(int id, String dateStr) {
        Microcontroller mc = mcRepository.findById(id).get();
        Date date = new Date();
        StringBuilder htmlString = new StringBuilder();
        int counter = 1;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        Date dateEnd = c.getTime();
        List<Data> dataList = dataRepository.findByMCIDTime(mc, date, dateEnd);

        for (Data data : dataList) {
            if (data.getTemperature() < 22 || data.getTemperature() > 25) {
                htmlString.append("<tr>\n" +
                        "            <td>" + counter + "</td>\n" +
                        "            <td>" + data.getDate_time() + "</td>\n" +
                        "            <td>Температура</td>\n" +
                        "            <td>Зафиксировано " + data.getTemperature() + " градусов Целься при установленной норме 22-25 градуса Цельсия</td>\n" +
                        "        </tr>\n");
                counter++;
            }
            if (data.getHumidity() < 40 || data.getHumidity() > 60) {
                htmlString.append("<tr>\n" +
                        "            <td>" + counter + "</td>\n" +
                        "            <td>" + data.getDate_time() + "</td>\n" +
                        "            <td>Влажность</td>\n" +
                        "            <td>Зафиксировано " + data.getHumidity() + "% влажности  при установленной норме 40-60%</td>\n" +
                        "        </tr>\n");
                counter++;
            }
            if (data.getCo2() < 800 || data.getCo2() > 1400) {
                htmlString.append("<tr>\n" +
                        "            <td>" + counter + "</td>\n" +
                        "            <td>" + data.getDate_time() + "</td>\n" +
                        "            <td>Углекислый газ</td>\n" +
                        "            <td>Зафиксировано " + data.getCo2() + "ppm  при установленной норме 800-1400ppm</td>\n" +
                        "        </tr>\n");
                counter++;
            }
            if (data.getLight() < 200 || data.getLight() > 350) {
                htmlString.append("<tr>\n" +
                        "            <td>" + counter + "</td>\n" +
                        "            <td>" + data.getDate_time() + "</td>\n" +
                        "            <td>Освещённость</td>\n" +
                        "            <td>Зафиксировано " + data.getLight() + " люминов влажности  при установленной норме 200-350 люминов</td>\n" +
                        "        </tr>\n");
                counter++;
            }
        }

        return htmlString.toString();
    }
}
