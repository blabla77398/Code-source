package sn.imc.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.imc.bean.MonIMC;
import sn.imc.dao.PatientDAO;

@WebServlet(urlPatterns = "/patients", loadOnStartup = 1)
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PatientDAO dao;

    /** La liste en mémoire est remplacée par l'accès à la base de données. */
    @Override
    public void init() throws ServletException {
        dao = new PatientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "liste";
        }

        try {
            switch (action) {

                case "supprimer": {
                    int id = lireId(request.getParameter("id"));
                    if (id > 0) {
                        dao.supprimer(id);
                    }
                    versListe(request, response);
                    break;
                }

                case "ajouter":
                    request.getRequestDispatcher("/ajouterPatient.jsp").forward(request, response);
                    break;

                case "modifier": {
                    int id = lireId(request.getParameter("id"));
                    MonIMC p = (id > 0) ? dao.trouver(id) : null;
                    if (p == null) {
                        versListe(request, response);
                        break;
                    }
                    request.setAttribute("patient", p);
                    request.setAttribute("id", id);
                    request.getRequestDispatcher("/ajouterPatient.jsp").forward(request, response);
                    break;
                }

                default:
                    request.setAttribute("patients", dao.lister());
                    request.getRequestDispatcher("/listeIMC.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }
    }

    /**
     * Crée un Bean MonIMC à partir du formulaire et l'enregistre dans la base
     * (INSERT, ou UPDATE si un "id" est fourni).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nom = lire(request, "nom");
        String prenom = lire(request, "prenom");

        try {
            double masse = Double.parseDouble(lire(request, "masse").replace(',', '.'));
            double taille = Double.parseDouble(lire(request, "taille").replace(',', '.'));

            if (nom.isEmpty() || prenom.isEmpty() || masse <= 0 || taille <= 0) {
                throw new NumberFormatException();
            }

            MonIMC p = new MonIMC(nom, prenom, masse, taille);

            int id = lireId(request.getParameter("id"));
            if (id > 0) {
                p.setId(id);
                dao.modifier(p);
            } else {
                dao.ajouter(p);
            }
            versListe(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("erreur",
                    "Veuillez renseigner un nom, un prénom, une masse et une taille valides.");
            request.getRequestDispatcher("/ajouterPatient.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }
    }

    // --- Méthodes utilitaires ---

    private String lire(HttpServletRequest request, String nomParametre) {
        String v = request.getParameter(nomParametre);
        return v == null ? "" : v.trim();
    }

    /** Retourne l'identifiant, ou -1 s'il est absent ou invalide. */
    private int lireId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void versListe(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/patients");
    }
}