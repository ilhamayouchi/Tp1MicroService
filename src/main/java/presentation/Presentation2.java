package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Presentation2 {

    public static void main(String[] args) {
        try {
            // 1. Charger les noms de classes depuis le fichier de configuration
            String[] classNames = loadClassNames("config.txt");

            // 2. Instancier le DAO
            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());

            // 3. Instancier le Métier
            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());

            // 4. Injecter le DAO dans le Métier via la réflexion
            injectDependency(metier, "setDao", dao);

            // 5. Exécuter le calcul
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lire les phrases depuis le fichier config.txt
    private static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        InputStream is = Presentation2.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new FileNotFoundException(fileName + " introuvable dans le classpath");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            for (int i = 0; i < classNames.length; i++) {
                String line = reader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    throw new IOException("config.txt : ligne " + (i + 1) + " manquante ou vide");
                }
                classNames[i] = line.trim();
            }
        }
        return classNames;
    }
    // Instanciation du classe à partir de son nom complet et releve un exception
    private static Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }
    // Injecte un dépandance via une methode setter
    private static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        Class<?> targetClass = target.getClass();
        Method setterMethod = targetClass.getMethod(setterName, dependency.getClass().getInterfaces()[0]);
        setterMethod.invoke(target, dependency);
    }
}