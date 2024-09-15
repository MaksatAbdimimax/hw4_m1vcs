import java.util.Random;

public class Main {
    public static int bossHealth = 650;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 280, 250, 300, 500, 200, 220}; //
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 25, 0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher"};

    public static int roundNumber = 0;
    public static final int HEAL_AMOUNT = 50; // Количество здоровья, которое лечит медик


    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossAttack();
        heroesAttack();
        medicHeals();
        takeHitGolem();
        witcherRevive();
        printStatistics();
    }


    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0, 1, 2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossAttack() {
        Random random = new Random();
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (i == 5) { // Проверяем, если это Lucky
                    boolean isLuckyDodged = random.nextInt(100) < 20; // 20% шанс уклонения
                    if (isLuckyDodged) {
                        System.out.println("Lucky dodged the attack!");
                        continue; // Lucky уклонился, не получает урона
                    }
                }
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }


    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && heroesDamage[i] > 0) { // Witcher не атакует
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical Damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }
    public static void witcherRevive() {
        if (heroesHealth[6] > 0) { // Проверяем, жив ли Witcher
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] == 0 && i != 6) { // Ищем первого погибшего героя, кроме Witcher
                    heroesHealth[i] = heroesHealth[6]; // Оживляем героя с текущим здоровьем Witcher
                    heroesHealth[6] = 0; // Witcher жертвует собой
                    System.out.println("Witcher revived " + heroesAttackType[i] + " and sacrificed himself.");
                    break; // Witcher может оживить только одного героя
                }
            }
        }
    }


    // Метод для лечения медика
    public static void medicHeals() {
        if (heroesHealth[3] > 0) { // Проверяем, жив ли медик
            for (int i = 0; i < heroesHealth.length; i++) {
                if (i != 3 && heroesHealth[i] > 0 && heroesHealth[i] < 100) { // Медик не может лечить себя
                    heroesHealth[i] += HEAL_AMOUNT;
                    System.out.println("Medic heals " + heroesAttackType[i] + " for " + HEAL_AMOUNT + " healths.");
                    break; // Лечит только одного героя за раунд
                }
            }
        }
    }
    // Metod sposobnost Golema
    public static void takeHitGolem() {
        if (heroesHealth[4] > 0) { // Проверяем, жив ли Golem
            int damageReduction = bossDamage / 5; // Golem принимает на себя 1/5 урона
            for (int i = 0; i < heroesHealth.length; i++) {
                if (i != 4 && heroesHealth[i] > 0) { // Golem не принимает урон за себя
                    int reducedDamage = bossDamage - damageReduction;
                    heroesHealth[i] = Math.max(heroesHealth[i] - reducedDamage, 0); // Герои получают уменьшенный урон
                }
            }
            heroesHealth[4] = Math.max(heroesHealth[4] - (damageReduction * (heroesHealth.length - 1)), 0); // Golem принимает 1/5 урона за каждого героя
            System.out.println("Golem took " + (damageReduction * (heroesHealth.length - 1)) + " damage.");
        }
    }


    public static void printStatistics() {
        System.out.println("ROUND: " + roundNumber + " ------------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " " +
                "defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
    }

}
