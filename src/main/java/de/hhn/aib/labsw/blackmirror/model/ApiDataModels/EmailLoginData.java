package de.hhn.aib.labsw.blackmirror.model.ApiDataModels;

/**
 * @author Markus Marewitz
 * @version 2022-05-11
 */
public record EmailLoginData (
        String host,
        int port,
        String username,
        String password
) {
}
