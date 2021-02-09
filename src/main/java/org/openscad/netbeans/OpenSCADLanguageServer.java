/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openscad.netbeans;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.lsp.client.spi.LanguageServerProvider;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;

/**
 *
 * @author Johannes Walcher
 */
@MimeRegistration(mimeType="application/x-openscad", service=LanguageServerProvider.class)
public class OpenSCADLanguageServer implements LanguageServerProvider {
    public static final Logger log = Logger.getLogger("OpenSCAD");
    public static final String openscad_executable = "openscad";

    @Override
    public LanguageServerDescription startServer(Lookup lkp) {
        try {
            log.info("Connecting to a running OpenSCAD instance");

            Process process = new ProcessBuilder(new String[] {openscad_executable, "--lsp-listen", "23725"})
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start();

            Socket client = new Socket(InetAddress.getLoopbackAddress(), 23725);
            return LanguageServerDescription.create(client.getInputStream(), client.getOutputStream(), process);
       } catch (IOException ex) {
           Exceptions.printStackTrace(ex);
           return null;
       }
    }
}
