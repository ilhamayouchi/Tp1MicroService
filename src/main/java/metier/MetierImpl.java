package metier;

import dao.IDao;

public class MetierImpl implements IMetier {

    // Dépendance injectable
    private IDao dao;

    // Methode d'injection pour l'instancier le dao à injecter
    public void setDao(IDao dao) {
        this.dao = dao;
    }
    //Implementation du calcul
    @Override
    public double calcul() {
        if (dao == null) {
            throw new IllegalStateException("Le DAO n'a pas été injecté.");
        }

        // Récupère la valeur depuis le DAO
        double valeurBase = dao.getValue();

        // Logique métier : double la valeur
        double resultat = valeurBase * 2;

        System.out.println("[METIER] Calcul effectué. Résultat : " + resultat);
        return resultat;
    }
}

