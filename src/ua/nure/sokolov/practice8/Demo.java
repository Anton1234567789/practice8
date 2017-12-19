package ua.nure.sokolov.practice8;

import ua.nure.sokolov.practice8.entity.Group;
import ua.nure.sokolov.practice8.entity.User;

import java.sql.SQLException;
import java.util.List;

public class Demo {
    private static <T> void printList(List<T> list) {
        for (T element : list) {
            System.out.println(element);
        }
    }

    public static void main(String[] args) throws SQLException, DBException {
        // users  ==> [ivanov]; groups ==> [teamA]

        DBManager dbManager = DBManager.getInstance();

        // Part 1
        dbManager.insertUser(User.createUser("petrov"));
        dbManager.insertUser(User.createUser("obama"));

        printList(dbManager.findAllUsers());

        //Part 2
        dbManager.insertGroup(Group.createGroup("teamA"));
        dbManager.insertGroup(Group.createGroup("teamC"));

        printList(dbManager.findAllGroups());

        //Part 3

        User userPetrov = dbManager.findUserByLogin("petrov");
        User userIvanov = dbManager.findUserByLogin("ivanov");
        User userObama = dbManager.findUserByLogin("obama");

        Group teamA = dbManager.findGroupByName("teamA");
        Group teamB = dbManager.findGroupByName("teamB");
        Group teamC = dbManager.findGroupByName("teamC");

        // method setGroupsForUser must implement transaction!
//        dbManager.setGroupsForUser(userPetrov, teamA);
        dbManager.setGroupsForUser(userObama, teamA, teamC);

        printList(dbManager.getUserGroup());

        System.out.println("===========================");
    }
}
