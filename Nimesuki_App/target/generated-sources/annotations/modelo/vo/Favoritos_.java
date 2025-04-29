package modelo.vo;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.vo.Anime;
import modelo.vo.FavoritosPK;
import modelo.vo.Usuario;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-04-29T12:45:34", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Favoritos.class)
public class Favoritos_ { 

    public static volatile SingularAttribute<Favoritos, FavoritosPK> favoritosPK;
    public static volatile SingularAttribute<Favoritos, Integer> capActual;
    public static volatile SingularAttribute<Favoritos, Integer> valoracion;
    public static volatile SingularAttribute<Favoritos, Usuario> usuario;
    public static volatile SingularAttribute<Favoritos, Anime> anime;

}