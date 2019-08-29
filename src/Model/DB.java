package Model;

import java.util.ArrayList;

public class DB {
    public static ArrayList<MemberTM> members=new ArrayList<>();
    public static ArrayList<BookTM> books=new ArrayList<>();
    public static ArrayList<IssueTM> issued=new ArrayList<>();
    public static ArrayList<ReturnTM> returned=new ArrayList<>();

    static {

        members.add(new MemberTM("M001","Ashan","Gampaha","0775874849"));
        members.add(new MemberTM("M002","Chamalki","Delgoda","0775124546"));

        books.add(new BookTM("B001","Sherlock","Arther Conendoil","Available"));
        books.add(new BookTM("B002","Lantern","James Smith","Available"));

        //    issued.add(new IssueTM("I001","25/02/2019","M001","B001"));

     //   returned.add(new ReturnTM("I001","2019/02/12","2019/05/15",15.5f));
    }
}
