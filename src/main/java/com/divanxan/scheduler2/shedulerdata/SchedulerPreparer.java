package com.divanxan.scheduler2.shedulerdata;

import com.divanxan.scheduler2.dto.CarDto;
import com.divanxan.scheduler2.dto.UserDto;
import com.divanxan.scheduler2.dto.WorkDatesDto;
import com.divanxan.scheduler2.model.Duty;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchedulerPreparer {


    public static List<UserDto> createScheduler(List<List<SchedulerData>> data, List<UserDto> users, List<CarDto> cars) {

        List<UserDto> specialUser = users.stream().filter(p -> p.getDuty().equals(Duty.DOCTOR)).sorted().collect(Collectors.toList());

        while (!specialUser.isEmpty()) {
            if (!specialUser.get(0).getWorkDatesDesireds().isEmpty()) addData(data, specialUser.get(0), cars);
            else specialUser.remove(0);
            specialUser = specialUser.stream().sorted().collect(Collectors.toList());
        }

        phillData(data, users, cars);
        List<SchedulerData> emptyData;
        List<UserDto> listEraser = new ArrayList<>();
        boolean end = false;
        int a = 1;
        while (!end) {
            end = true;
            emptyData = getSchedulerData(data);
            for (SchedulerData noData : emptyData) {
                Map<Long, WorkDatesDto> map = new HashMap<>();
                eraseData(data, users, noData, listEraser, map);
                listEraser = listEraser.stream().sorted().collect(Collectors.toList());
                UserDto changedUser = listEraser.get(listEraser.size() - 1);
                WorkDatesDto changedDate = map.get(changedUser.getId());
                if (changedDate!=null) {
                    int number = Math.toIntExact(changedDate.getCar().getId() - 1);
                    int numberOfDate = Math.toIntExact(changedDate.getDate().getDayOfMonth() - 1);
                    SchedulerData dataSh = data.get(number).get(numberOfDate);
                    dataSh.setDoctor(null);
                    noData.setDoctor(changedUser);
                    changedUser.setHonest(changedUser.getHonest() + 2);
                    changedUser.getWorkDates().remove(changedDate);

                    WorkDatesDto workDates = new WorkDatesDto();
                    workDates.setDate(noData.getDate());
                    workDates.setCar(cars.get(noData.getCar() - 1));
                    changedUser.getWorkDates().add(workDates);
                }
            }

            phillData(data, users, cars);

            a++;
            for (List<SchedulerData> dam : data) {
                for (SchedulerData dat : dam) {
                    if (dat.getDoctor() == null) {
                        end = false;
                        break;
                    }
                }
            }
            System.out.println(a);
        }

        return users.stream().filter(p -> p.getDuty().equals(Duty.DOCTOR)).sorted().collect(Collectors.toList());

    }

    private static void eraseData(List<List<SchedulerData>> data, List<UserDto> users, SchedulerData schedulerData
            , List<UserDto> listEraser, Map<Long, WorkDatesDto> map) {

        int a = schedulerData.getDay();
        for (UserDto user : users) {
            List<WorkDatesDto> workDatesDto = user.getWorkDates();
            for (int i = 0; i < workDatesDto.size(); i++) {
                WorkDatesDto dto = workDatesDto.get(i);
                if (((a - dto.getDate().getDayOfMonth()) <= 1 && (dto.getDate().getDayOfMonth() - a) <= -1)
                        || (((dto.getDate().getDayOfMonth() - a) <= 1) && (a - dto.getDate().getDayOfMonth()) <= 1)) {
                    if ((a - dto.getDate().getDayOfMonth()) != 0) {
                        if (((a - workDatesDto.get(i - 1).getDate().getDayOfMonth()) > 2)
                                && ((workDatesDto.get(i + 1).getDate().getDayOfMonth() - a) > 2))
                            listEraser.add(user);
                        map.put(user.getId(), workDatesDto.get(i));
                    }
                    break;
                }
            }
        }
    }

    private static void phillData(List<List<SchedulerData>> data, List<UserDto> users, List<CarDto> cars) {
        List<UserDto> specialUser;
        specialUser = users.stream().filter(p -> p.getDuty().equals(Duty.DOCTOR))
                .sorted().collect(Collectors.toList());
        List<SchedulerData> emptyData = getSchedulerData(data);

        Map<Integer, List<UserDto>> possibleDays1 = getMapDates(specialUser, emptyData);
        while (!possibleDays1.isEmpty()) {
            List<UserDto> list = possibleDays1.get(emptyData.get(0).getDay());
            if (list.size() == 1) {
                setWorkDate(cars, emptyData, list);
            } else if (list.size() > 1) {
                List<UserDto> listSort = list.stream().sorted().collect(Collectors.toList());
                setWorkDate(cars, emptyData, listSort);
            }
            emptyData.remove(0);
            possibleDays1 = getMapDates(specialUser, emptyData);
        }
    }

    private static List<SchedulerData> getSchedulerData(List<List<SchedulerData>> data) {
        Stream<SchedulerData> stream = data.get(0).stream();
        for (int i = 1; i < data.size(); i++) {
            stream = Stream.concat(stream, data.get(i).stream());
        }
        return stream.filter(p -> p.getDoctor() == null).sorted().collect(Collectors.toList());
    }

    private static void setWorkDate(List<CarDto> cars, List<SchedulerData> emptyData, List<UserDto> list) {
        emptyData.get(0).setDoctor(list.get(0));
        WorkDatesDto workDates = new WorkDatesDto();
        workDates.setDate(emptyData.get(0).getDate());
        workDates.setCar(cars.get(emptyData.get(0).getCar() - 1));
        list.get(0).getWorkDates().add(workDates);
        list.get(0).setShift(list.get(0).getShift() + 1);
    }

    private static Map<Integer, List<UserDto>> getMapDates(List<UserDto> specialUser, List<SchedulerData> emptyData) {
        Map<Integer, List<UserDto>> possibleDays = new TreeMap<>();
        for (SchedulerData day : emptyData) {
            List<UserDto> usAll = new ArrayList<>();
            for (UserDto user : specialUser) {
                if (user.getDatesPossible(31) == null || user.getDatesPossible(31).stream().anyMatch(p -> p.equals(day.getDay())))
                    usAll.add(user);
            }
            possibleDays.put(day.getDay(), usAll);
        }
        return possibleDays;
    }

    public static List<List<SchedulerData>> prepareScheduler(int cars, int days) {
        List<List<SchedulerData>> list = new ArrayList<>();
        for (int i = 0; i < cars; i++) {
            List<SchedulerData> listDays = new ArrayList<>();
            for (int j = 0; j < days; j++) {
                listDays.add(new SchedulerData(j + 1, i + 1));
            }
            list.add(listDays);
        }
        return list;
    }

    public static void addData(List<List<SchedulerData>> data, UserDto priorityUser, List<CarDto> cars) {

        int day = priorityUser.getWorkDatesDesireds().get(0).getDate().getDayOfMonth();
        boolean isSet = false;
        for (int i = 0; i < data.size(); i++) {
            SchedulerData dataS = data.get(i).get(day - 1);
            if (priorityUser.getDuty().equals(Duty.DOCTOR)) {
                if (dataS.getDoctor() == null) {
                    dataS.setDoctor(priorityUser);
                    isSet = isSet(priorityUser, cars, i);
                    break;
                }
            } else if (priorityUser.getDuty().equals(Duty.DRIVER)) {
                if (dataS.getDriver() == null) {
                    dataS.setDriver(priorityUser);
                    isSet = isSet(priorityUser, cars, i);
                    break;
                }
            } else {
                if (dataS.getParamedic() == null) {
                    dataS.setParamedic(priorityUser);
                    isSet = isSet(priorityUser, cars, i);
                    break;
                }
            }
        }
        if (!isSet) {
            priorityUser.setHonest(priorityUser.getHonest() + data.size());
            priorityUser.getWorkDatesDesireds().remove(0);
        } else {
            priorityUser.setHonest(priorityUser.getHonest() - 1);
        }
    }

    private static boolean isSet(UserDto priorityUser, List<CarDto> cars, int i) {
        boolean isSet;
        WorkDatesDto workDates = new WorkDatesDto();
        workDates.setDate(priorityUser.getWorkDatesDesireds().get(0).getDate());
        workDates.setCar(cars.get(i));
        priorityUser.setShift(priorityUser.getShift() + 1);
        priorityUser.getWorkDates().add(workDates);
        priorityUser.getWorkDatesDesireds().remove(0);
        isSet = true;
        return isSet;
    }
}
