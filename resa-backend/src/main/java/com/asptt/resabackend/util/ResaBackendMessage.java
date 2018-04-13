package com.asptt.resabackend.util;

import org.springframework.stereotype.Component;

@Component
public interface ResaBackendMessage {

    public final String REGISTRATION_GENERIC = "Oups problème d'inscription à la plongée inconnu. Contacter l'administrateur";

    //BadUsageException business checkOrderForDive
    public final String REGISTRATION_ORDER_ACTION_NOT_FOUND = "order.action is mandatory";

    //BadUsageException business checkOrderForDive
    public final String REGISTRATION_ORDER_ADHERENTID_NOT_FOUND = "order.adherentId is mandatory";
    
    //NotFoundException adherent non trouvé avec son numero de license
    public final String ADHERENT_NOT_FOUND = "Aucun Adherent avec la license [{0}]";

    //NotFoundException plongée non trouvée avec son numero id
    public final String PLONGEE_NOT_FOUND = "Aucune plongée avec l''id [{0}]";

    //FunctionalException business isOkForResa
    public final String REGISTRATION_OKFORRESA_ADH_INSCRIT = "Désolé {0} mais tu es déjà inscrit à cette plongée";

    //FunctionalException business checkHeureOuverture
    public final String REGISTRATION_ATTENDRE_HEURE_OUVERT = "Désolé {0} il faudra attendre : le {1} à {2} heures pour t''inscrire.";

    //FunctionalException business checkHeureOuverture
    public final String REGISTRATION_ATTENDRE_J_HO = "Désolé {0} mais l''inscription ouvre à partir de {1} heure.";
    
    //FunctionalException business checkHeureOuverture
    public final String REGISTRATION_NB_MINI_PLONGEUR = "Désolé {0} mais le nombre minimum de participants pour cette plongée n''étant pas atteint (5), ta demande d''inscription ne peut pas être prise en compte. Cette plongée est annulée.";
    
    //FunctionalException business checkHeureOuverture
    public final String REGISTRATION_PLONGEE_FERMEE = "Inscription impossible sur la plongée du {1} {0} : Cette plongée n''est pas ouverte.";
    
}
