package sn.imc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sn.imc.bean.MonIMC;

/**
 * Accès à la base ListeIMC (table patient) avec JDBC.
 */
public class PatientDAO {

    // XAMPP : utilisateur "root" sans mot de passe par défaut, port 3306
    private static final String URL = "jdbc:mariadb://localhost:3306/ListeIMC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final String COLONNES = "id, nom, prenom, masse, taille, imc, interpretation";

    public PatientDAO() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Driver MariaDB introuvable : ajoute le jar dans WEB-INF/lib", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Retourne tous les patients. */
    public List<MonIMC> lister() throws SQLException {
        List<MonIMC> liste = new ArrayList<>();
        String sql = "SELECT " + COLONNES + " FROM patient ORDER BY id";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(construire(rs));
            }
        }
        return liste;
    }

    /** Retourne le patient d'identifiant id, ou null s'il n'existe pas. */
    public MonIMC trouver(int id) throws SQLException {
        String sql = "SELECT " + COLONNES + " FROM patient WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? construire(rs) : null;
            }
        }
    }

    public void ajouter(MonIMC p) throws SQLException {
        String sql = "INSERT INTO patient (nom, prenom, masse, taille, imc, interpretation) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDouble(3, p.getMasse());
            ps.setDouble(4, p.getTaille());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getInterpretation());
            ps.executeUpdate();
        }
    }

    public void modifier(MonIMC p) throws SQLException {
        String sql = "UPDATE patient SET nom = ?, prenom = ?, masse = ?, taille = ?, "
                   + "imc = ?, interpretation = ? WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setDouble(3, p.getMasse());
            ps.setDouble(4, p.getTaille());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getInterpretation());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM patient WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Transforme la ligne courante du ResultSet en bean MonIMC. */
    private MonIMC construire(ResultSet rs) throws SQLException {
        MonIMC p = new MonIMC();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setMasse(rs.getDouble("masse"));
        p.setTaille(rs.getDouble("taille"));
        p.setImc(rs.getDouble("imc"));
        p.setInterpretation(rs.getString("interpretation"));
        return p;
    }
}