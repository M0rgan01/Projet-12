package org.paniergarni.account.business;

import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.SendMailException;

/**
 * Manipulation de Mail
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface MailBusiness {

    /** Mise à jour d'un email
     *
     * @param id
     * @param email
     * @return Mail
     * @throws AccountException
     */
    Mail updateMail(Long id, String email) throws AccountException;

    /** Récupération d'un Mail par son ID
     *
     * @param id
     * @return Mail
     * @throws AccountException
     */
    Mail getMailById(Long id) throws AccountException;

    /** Récupération d'un Mail par son email
     *
     * @param email
     * @return Mail
     * @throws AccountException
     */
    Mail getMailByEmail(String email) throws AccountException;

    /** Vérification de la présence d'un attribut email similaire
     *
     * @param email
     * @throws AccountException
     */
    void checkEmailExist(String email) throws AccountException;

    /** Envoie un token à l'email renseigné pour le processus de récupération
     *
     * @param email
     * @return Mail
     * @throws AccountException
     * @throws SendMailException
     */
    Mail sendTokenForRecovery(String email) throws AccountException, SendMailException;

    /** Validation d'un token
     *
     * @param token
     * @param email
     * @return Mail
     * @throws AccountException
     */
    Mail validateToken(String token, String email)  throws AccountException;
}
