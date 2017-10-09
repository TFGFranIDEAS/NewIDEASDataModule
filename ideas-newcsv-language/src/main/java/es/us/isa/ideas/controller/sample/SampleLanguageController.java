package es.us.isa.ideas.controller.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import es.us.isa.ideas.module.common.AppResponse;
import es.us.isa.ideas.module.common.AppResponse.Status;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.us.isa.ideas.module.controller.BaseLanguageController;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/language")
public class SampleLanguageController extends BaseLanguageController {

    @RequestMapping(value = "/operation/{id}/execute", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse executeOperation(String id, String content, String fileUri, String auxArg0) {

        AppResponse appResponse = new AppResponse();
        appResponse.setFileUri(fileUri);

        if (id.equals("csv")) {
            String[] lines = content.split("\n");
            String[][] linesCsv = new String[lines.length][];

            for (int i=0; i<lines.length; i++) {
                linesCsv[i] = lines[i].split(",");
            }
            
            /*
            ScriptEngineManager script = new ScriptEngineManager();
            ScriptEngine js = script.getEngineByName("JavaScript");
            try {
                // evaluamos lineas java script
                js.eval("document.getElementById('editorInspectorLoader').innerHTML="+Arrays.deepToString(linesCsv));
            } catch (ScriptException ex) {
                System.out.println("Hubo un error:"+ex);
            }
            */
        }

        return appResponse;
    }

    @RequestMapping(value = "/format/{format}/checkLanguage", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse checkLanguage(String id, String content, String fileUri) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public AppResponse convertFormat(String currentFormat, String desiredFormat, String fileUri, String content) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
