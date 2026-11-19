package recursosrz.Repository.aspirantesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import recursosrz.Config.ConnectionDB;
import recursosrz.Response.aspirantesResponse.Aspirante;
import recursosrz.aspirnatesclaseDos.aspirantesDos;
import recursosrz.exception.databaseException;

public class aspirantesR {

    public List<Aspirante> findAll() {
        List<Aspirante> list = new ArrayList<>();
        String sql = "SELECT a.id, a.nombre, a.apellido, a.correo, a.telefono, a.edad, d.direccion_linea "
                + "FROM aspirantes a JOIN direcciones d ON a.direccion_id = d.id";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Aspirante(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion_linea"),
                        rs.getInt("edad")
                ));
            }
        } catch (SQLException e) {
            throw new databaseException("Error al consultar aspirantes", e);
        }
        return list;
    }

    public Aspirante save(aspirantesDos req) {
        String newUuid = UUID.randomUUID().toString();
        String sqlDir = "INSERT INTO direcciones (direccion_linea) VALUES (?)";
        String sqlAsp = "INSERT INTO aspirantes (id, nombre, apellido, correo, telefono, edad, direccion_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmDir = conn.prepareStatement(sqlDir, Statement.RETURN_GENERATED_KEYS)) {
                pstmDir.setString(1, req.getDireccion());
                pstmDir.executeUpdate();

                ResultSet rs = pstmDir.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Error al obtener ID de dirección");
                }
                int dirId = rs.getInt(1);

                try (PreparedStatement psAsp = conn.prepareStatement(sqlAsp)) {
                    psAsp.setString(1, newUuid);
                    psAsp.setString(2, req.getNombre());
                    psAsp.setString(3, req.getApellido());
                    psAsp.setString(4, req.getCorreo());
                    psAsp.setString(5, req.getTelefono());
                    psAsp.setInt(6, req.getEdad());
                    psAsp.setInt(7, dirId);
                    psAsp.executeUpdate();
                }

                conn.commit();
                return new Aspirante(
                        newUuid,
                        req.getNombre(),
                        req.getApellido(),
                        req.getCorreo(),
                        req.getTelefono(),
                        req.getDireccion(),
                        req.getEdad()
                );
            } catch (SQLException ex) {
                conn.rollback();
                throw new databaseException("Error al guardar aspirante", ex);
            }
        } catch (SQLException e) {
            throw new databaseException("Error de conexión al guardar aspirante", e);
        }
    }

    public Aspirante update(aspirantesDos req) {
        String sqlAsp = "UPDATE aspirantes SET nombre=?, apellido=?, correo=?, telefono=?, edad=? WHERE id=?";
        String sqlDir = "UPDATE direcciones d JOIN aspirantes a ON d.id = a.direccion_id SET d.direccion_linea=? WHERE a.id=?";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psAsp = conn.prepareStatement(sqlAsp);
                 PreparedStatement psDir = conn.prepareStatement(sqlDir)) {

                psAsp.setString(1, req.getNombre());
                psAsp.setString(2, req.getApellido());
                psAsp.setString(3, req.getCorreo());
                psAsp.setString(4, req.getTelefono());
                psAsp.setInt(5, req.getEdad());
                psAsp.setString(6, req.getIdAspirante());
                int rowsUpdated = psAsp.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    throw new databaseException("No se encontró el aspirante con ese ID.");
                }

                psDir.setString(1, req.getDireccion());
                psDir.setString(2, req.getIdAspirante());
                psDir.executeUpdate();

                conn.commit();
                return new Aspirante(
                        req.getIdAspirante(),
                        req.getNombre(),
                        req.getApellido(),
                        req.getCorreo(),
                        req.getTelefono(),
                        req.getDireccion(),
                        req.getEdad()
                );
            } catch (SQLException ex) {
                conn.rollback();
                throw new databaseException("Error al actualizar aspirante", ex);
            }
        } catch (SQLException e) {
            throw new databaseException("Error de conexión al actualizar aspirante", e);
        }
    }

    public Aspirante deleteById(String id) {
        String selectSql = "SELECT a.id, a.nombre, a.apellido, a.correo, a.telefono, a.edad, d.direccion_linea "
                + "FROM aspirantes a JOIN direcciones d ON a.direccion_id = d.id WHERE a.id = ?";
        String deleteSql = "DELETE FROM direcciones WHERE id = (SELECT direccion_id FROM aspirantes WHERE id = ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            Aspirante deletedItem = null;

            try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
                psSelect.setString(1, id);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        deletedItem = new Aspirante(
                                rs.getString("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("correo"),
                                rs.getString("telefono"),
                                rs.getString("direccion_linea"),
                                rs.getInt("edad")
                        );
                    }
                }
            }

            if (deletedItem != null) {
                try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
                    psDelete.setString(1, id);
                    psDelete.executeUpdate();
                }
            }

            conn.commit();
            return deletedItem;
        } catch (SQLException ex) {
            throw new databaseException("Error al eliminar aspirante", ex);
        }
    }
}

    
