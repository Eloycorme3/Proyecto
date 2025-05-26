package modelo.vo;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.vo.Favoritos;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-05-26T13:13:40", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Anime.class)
public class Anime_ { 

    public static volatile SingularAttribute<Anime, String> descripcion;
    public static volatile SingularAttribute<Anime, Integer> idAnime;
    public static volatile SingularAttribute<Anime, String> categorias;
    public static volatile SingularAttribute<Anime, Integer> anhoSalida;
    public static volatile SingularAttribute<Anime, String> imagen;
    public static volatile ListAttribute<Anime, Favoritos> favoritosList;
    public static volatile SingularAttribute<Anime, String> nombre;
    public static volatile SingularAttribute<Anime, Integer> capTotales;

}