package co.edu.poli.cnt.simulador.servicio.entidades;

import co.edu.poli.cnt.simulador.modelo.PreguntaEntity;
import co.edu.poli.cnt.simulador.modelo.OpcionRespuestaEntity;
import co.edu.poli.cnt.simulador.modelo.TipoPregunta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.junit.Test;

/**
 * Prueba de la conexión con la base de datos
 *
 * @author Antonio Paternina <acpaternina@poli.edu.co>
 */
public class DerbyTest {

    private final Logger logger = Logger.getLogger(DerbyTest.class);

    private static EntityManager em;

    @BeforeClass
    public static void crearEntityManager() {
        em = initHibernate();
    }

    @AfterClass
    public static void limpiarBaseDeDatos() {
        em.getTransaction().begin();
        em.createQuery("delete from OpcionRespuestaEntity").executeUpdate();
        em.createQuery("delete from PreguntaEntity").executeUpdate();
        em.createQuery("delete from TemaCertificacionEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void probarBaseDeDatos() {
        em = initHibernate();
        em.getTransaction().begin();
        crearEntidades(em);
        listarEntidades(em);
        em.getTransaction().commit();
    }

    private void crearEntidades(final EntityManager em) {
        PreguntaEntity pregunta = new PreguntaEntity();
        pregunta.setTextoInicio("Contenido de prueba");
        pregunta.setFragmentoCodigo("Fragmento de código de prueba");
        pregunta.setTipoPregunta(TipoPregunta.SELECCION_UNICA);

        List<OpcionRespuestaEntity> respuestas = new ArrayList<>();
        OpcionRespuestaEntity respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(true);
        respuesta.setOrden(1);
        respuestas.add(respuesta);
        respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(false);
        respuesta.setOrden(2);
        respuestas.add(respuesta);
        respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(false);
        respuestas.add(respuesta);

        pregunta.setOpcionesRespuesta(respuestas);

        em.persist(pregunta);

        pregunta = new PreguntaEntity();
        pregunta.setTextoInicio("Contenido de prueba");
        pregunta.setFragmentoCodigo("Fragmento de código de prueba");
        pregunta.setTipoPregunta(TipoPregunta.SELECCION_MULTIPLE);

        respuestas = new ArrayList<>();
        respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(true);
        respuesta.setOrden(1);
        respuestas.add(respuesta);
        respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(false);
        respuesta.setOrden(2);
        respuestas.add(respuesta);
        respuesta = new OpcionRespuestaEntity();
        respuesta.setContenido("contenido de prueba");
        respuesta.setOpcionCorrecta(true);
        respuestas.add(respuesta);

        pregunta.setOpcionesRespuesta(respuestas);

        em.persist(pregunta);
    }

    private void listarEntidades(final EntityManager em) {
        Query query;
        query = em.createQuery("from " + PreguntaEntity.class.getName());
        List<PreguntaEntity> preguntaEntityList;
        preguntaEntityList = query.getResultList();
        Assert.assertEquals("El número de preguntas retornado no es correcto", 2, preguntaEntityList.size());
        logger.info("El Query de Preguntas retornó " + preguntaEntityList.size() + " resultados");
        preguntaEntityList.stream().forEach((row) -> {
            logger.debug(row.toString());
        });

        query = em.createQuery("from " + OpcionRespuestaEntity.class.getName());
        List<OpcionRespuestaEntity> opcionRespuestaEntityList;
        opcionRespuestaEntityList = query.getResultList();
        Assert.assertEquals("El número de respuestas retornado no es correcto", 6, opcionRespuestaEntityList.size());
        logger.info("El Query de Opciones de Respuesta retornó " + opcionRespuestaEntityList.size() + " resultados");
        opcionRespuestaEntityList.stream().forEach((row) -> {
            logger.debug(row.toString());
        });
    }

    public static EntityManager initHibernate() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("co.edu.poli.cnt_SimuladorCertificacion");
        return emf.createEntityManager();
    }
}
