package bo.com.paddingdata.Tool;

import java.util.Date;
import java.util.Random;

/**
 * Created by bo.zhang on 2017/05/04   .
 */

public class RandomData {

    //产生一个随机邮箱账号
    public static String getRandomEmail() {
        int length = 12;
        int count = (int) (3 + Math.random() * (length - 3));
        int count1 = (int) (1 + Math.random() * (count - 3));
        String randomEmail = "";
        char[] alphaEmail = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] alphaTLD = {".com", ".net", ".org", ".biz", ".mobi", ".travel", ".name", ".pro",
                ".cn", ".cc", ".co", ".in", ".me", ".tv", ".us", ".tk", ".net", ".gov", ".edu",
                ".mil", ".biz", ".info", ".museum", ".travel", ".int", ".areo", ".post", ".rec",
                ".asia", ".vip", ".top", ".au", ".ae", ".ar", ".br", ".ca", ".ch", ".de", ".dk",
                ".es", ".eg", ".fg", ".gr", ".hk", ".in", ".iq", ".it", ".jp", ".mo", ".us", ".uk"};
        for (int i = 0; i < count1; i++) {
            Character ranChar1 = Character.valueOf(alphaEmail[new Random().nextInt(alphaEmail.length)]);
            randomEmail = randomEmail + ranChar1.toString();
        }
        randomEmail = randomEmail + "@";
        for (int a = 0; a < count - count1 - 1; a++) {
            Character ranChar2 = Character.valueOf(alphaEmail[new Random().nextInt(alphaEmail.length)]);
            randomEmail = randomEmail + ranChar2.toString();
        }
        String emailEnding = alphaTLD[new Random().nextInt(alphaTLD.length)];
        randomEmail = randomEmail + emailEnding.toString();
        System.out.println(randomEmail);
        return randomEmail;
    }

    public static String getRandomContactName() {
        final String[] FEMALE_FIRST_NAMES = {

                "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan",

                "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra", "Donna",

                "Carol", "Ruth", "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica",

                "Shirley", "Cynthia", "Angela", "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Virginia",

                "Kathleen", "Pamela", "Martha", "Debra", "Amanda", "Stephanie", "Carolyn", "Christine",

                "Marie", "Janet", "Catherine", "Frances", "Ann", "Joyce", "Diane", "Alice", "Julie",

                "Heather", "Teresa", "Doris", "Gloria", "Evelyn", "Jean", "Cheryl", "Mildred", "Katherine",

                "Joan", "Ashley", "Judith", "Rose", "Janice", "Kelly", "Nicole", "Judy", "Christina",

                "Kathy", "Theresa", "Beverly", "Denise", "Tammy", "Irene", "Jane", "Lori", "Rachel",

                "Marilyn", "Andrea", "Kathryn", "Louise", "Sara", "Anne", "Jacqueline", "Wanda", "Bonnie",

                "Julia", "Ruby", "Lois", "Tina", "Phyllis", "Norma", "Paula", "Diana", "Annie", "Lillian",

                "Emily", "Robin", "Peggy", "Crystal", "Gladys", "Rita", "Dawn", "Connie", "Florence",

                "Tracy", "Edna", "Tiffany", "Carmen", "Rosa", "Cindy", "Grace", "Wendy", "Victoria", "Edith",

                "Kim", "Sherry", "Sylvia", "Josephine", "Thelma", "Shannon", "Sheila", "Ethel", "Ellen",

                "Elaine", "Marjorie", "Carrie", "Charlotte", "Monica", "Esther", "Pauline", "Emma",

                "Juanita", "Anita", "Rhonda", "Hazel", "Amber", "Eva", "Debbie", "April", "Leslie", "Clara",

                "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie", "Danielle", "Megan", "Alicia", "Suzanne",

                "Michele", "Gail", "Bertha", "Darlene", "Veronica", "Jill", "Erin", "Geraldine", "Lauren",

                "Cathy", "Joann", "Lorraine", "Lynn", "Sally", "Regina", "Erica", "Beatrice", "Dolores",

                "Bernice", "Audrey", "Yvonne", "Annette", "June", "Samantha", "Marion", "Dana", "Stacy",

                "Ana", "Renee", "Ida", "Vivian", "Roberta", "Holly", "Brittany", "Melanie", "Loretta",

                "Yolanda", "Jeanette", "Laurie", "Katie", "Kristen", "Vanessa", "Alma", "Sue", "Elsie",

                "Beth", "Jeanne"};

        final String[] LAST_NAMES = {

                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",

                "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia",

                "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen",

                "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker",

                "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips",

                "Campbell", "Parker", "Evans", "Edwards", "Collins", "Stewart", "Sanchez", "Morris",

                "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper",

                "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez", "James",

                "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross",

                "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes",

                "Flores", "Washington", "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander",

                "Russell", "Griffin", "Diaz", "Hayes", "Myers", "Ford", "Hamilton", "Graham", "Sullivan",

                "Wallace", "Woods", "Cole", "West", "Jordan", "Owens", "Reynolds", "Fisher", "Ellis",

                "Harrison", "Gibson", "Mcdonald", "Cruz", "Marshall", "Ortiz", "Gomez", "Murray", "Freeman",

                "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter", "Hunter", "Hicks", "Crawford",

                "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon", "Ramos", "Reyes", "Burns",

                "Gordon", "Shaw"

                , "Holmes", "Rice", "Robertson", "Hunt", "Black", "Daniels", "Palmer",

                "Mills", "Nichols", "Grant", "Knight", "Ferguson", "Rose", "Stone", "Hawkins", "Dunn",

                "Perkins", "Hudson", "Spencer", "Gardner", "Stephens", "Payne", "Pierce", "Berry",

                "Matthews", "Arnold", "Wagner", "Willis", "Ray", "Watkins", "Olson", "Carroll", "Duncan",

                "Snyder", "Hart", "Cunningham", "Bradley", "Lane", "Andrews", "Ruiz", "Harper", "Fox",

                "Riley", "Armstrong", "Carpenter", "Weaver", "Greene", "Lawrence", "Elliott", "Chavez",

                "Sims", "Austin", "Peters", "Kelley", "Franklin", "Lawson"};
        return FEMALE_FIRST_NAMES[(int) (Math.random() * (FEMALE_FIRST_NAMES.length - 1))] + " " +
                LAST_NAMES[(int) (Math.random() * (LAST_NAMES.length - 1))];
    }

    public static String getRandomContactNote() {
        int Length = 40;
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String RandomString = "";
        for (int i = 0; i < Length; i++) {
            int randomLength = (int) (Math.random() * (chars.length - 1));
            RandomString = RandomString + chars[randomLength];
        }
        return RandomString;
    }

    public static String getRandomContactPhone() {
        int Length = 11;
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String RandomString = "";
        for (int i = 0; i < Length; i++) {
            int randomLength = (int) (Math.random() * (chars.length - 1));
            RandomString = RandomString + chars[randomLength];
        }
        return RandomString;
    }
}
