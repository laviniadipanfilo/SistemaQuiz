package it.sistemaquiz.ordinaLista;

import org.springframework.stereotype.Service;

import it.sistemaquiz.entity.JSONResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.reflect.Method;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Service
public class CodiceService {

	public JSONResponse compila(String funzioneUtente) {
	    try {
	        String codiceCompleto = funzioneUtente;

//			ottengo il compilatore e compilo
	        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        JavaFileObject file = new StringJavaFileObject("CodiceUtente", codiceCompleto);
	        DiagnosticCollector<JavaFileObject> diagnostica = new DiagnosticCollector<>();
	        FileManagerInMemoria fileManager = new FileManagerInMemoria(compiler.getStandardFileManager(null, null, null));

	        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostica, null, null, List.of(file));
	        boolean riuscita = task.call();

//			se ci sono errori, vengono stampati
	        if (!riuscita) {
	            StringBuilder messaggioErrore = new StringBuilder();
	            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostica.getDiagnostics()) {
	                messaggioErrore.append("Errore alla riga ").append(diagnostic.getLineNumber())
	                        .append(" nella classe ").append(diagnostic.getSource().getName()).append(": ")
	                        .append(diagnostic.getMessage(null)).append("\n");
	            }
	            return new JSONResponse("Errore", messaggioErrore.toString());
	        }

	        return new JSONResponse("Successo", "Compilazione avvenuta con successo.");
	    } catch (Exception e) {
	        return new JSONResponse("Errore", "Errore durante la compilazione: " + e.getMessage());
	    }
	}


	public List<Map<String, String>> eseguiCodiceCompilato(String funzioneUtente, List<Map<String, Object>> testCases) {
	    try {
	        String codiceCompleto = funzioneUtente;

	        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        JavaFileObject file = new StringJavaFileObject("CodiceUtente", codiceCompleto);
	        DiagnosticCollector<JavaFileObject> diagnostica = new DiagnosticCollector<>();
	        FileManagerInMemoria fileManager = new FileManagerInMemoria(compiler.getStandardFileManager(null, null, null));

	        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostica, null, null, List.of(file));
	        boolean riuscita = task.call();

	        if (!riuscita) {
	            throw new IllegalStateException("Errore di compilazione.");
	        }

	        Map<String, byte[]> classBytes = fileManager.getBytes();
	        CaricatoreClassiInMemoria classLoader = new CaricatoreClassiInMemoria(classBytes);
	        Class<?> classeUtente = classLoader.loadClass("CodiceUtente");

	        List<Map<String, String>> risultati = new LinkedList<>();
	        for (int i = 0; i < testCases.size(); i++) {
	            Map<String, Object> testCase = testCases.get(i);
	            String nomeMetodo = (String) testCase.get("method");

	            List<Object> parametriLista = (List<Object>) testCase.get("params");
	            Object[] parametri = parametriLista.toArray();

	            System.out.println("Metodo " + nomeMetodo + " con parametri: " + Arrays.toString(parametri));

	            Method metodoUtente = trovaMetodo(classeUtente, nomeMetodo, parametri);
	            Object result = metodoUtente.invoke(null, parametri);

	            System.out.println("Risultato: " + result);

	            Object expectedOutput = testCase.get("expected");

	            Map<String, String> risultato = new HashMap<>();
	            risultato.put("test case", "Test Case " + (i + 1));
	            risultato.put("valore aspettato", expectedOutput.toString());
	            risultato.put("valore reale", result != null ? result.toString() : "null");

	            if (result.equals(expectedOutput)) {
	                risultato.put("risultato", "GIUSTO");
	            } else {
	                risultato.put("risultato", "SBAGLIATO");
	            }

	            risultati.add(risultato);
	        }

	        return risultati;

	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, String> errore = new HashMap<>();
	        errore.put("errore", "Errore durante l'esecuzione: " + e.getMessage());
	        return List.of(errore);
	    }
	}


//  trova un metodo con nome e parametri specifici
	private Method trovaMetodo(Class<?> classe, String metodo, Object[] parametri) throws NoSuchMethodException {
	    Class<?>[] tipiParametri = Arrays.stream(parametri)
	            .map(param -> {
	                if (param instanceof Integer) {
	                    return int.class;
	                } else {
	                    return param.getClass();
	                }
	            })
	            .toArray(Class[]::new);

	    return classe.getMethod(metodo, tipiParametri);
	}

 // Classe che rappresenta il file Java dinamico in memoria
    class StringJavaFileObject extends SimpleJavaFileObject {
        private final String codice;

        // Correzione del nome del file con una gestione corretta delle estensioni
        protected StringJavaFileObject(String nomeClasse, String codice) {
            super(URI.create("string:///" + nomeClasse + Kind.SOURCE.extension), Kind.SOURCE);
            this.codice = codice;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return codice;
        }
    }

//  file manager in memoria per gestire i bytecode
    private static class FileManagerInMemoria extends ForwardingJavaFileManager<JavaFileManager> {
        private final Map<String, Bytecode> bytecodeMap = new HashMap<>();

        protected FileManagerInMemoria(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            Bytecode bytecode = new Bytecode(className);
            bytecodeMap.put(className, bytecode);
            return bytecode;
        }

        public Map<String, byte[]> getBytes() {
            Map<String, byte[]> result = new HashMap<>();
            for (Map.Entry<String, Bytecode> entry : bytecodeMap.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getBytes());
            }
            return result;
        }
    }

//  classe che rappresenta i bytecode
    private static class Bytecode extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        protected Bytecode(String name) {
            super(URI.create("bytes:///" + name), Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }

//  classe che carica i byte in memoria
    private static class CaricatoreClassiInMemoria extends ClassLoader {
        private final Map<String, byte[]> classBytes;

        public CaricatoreClassiInMemoria(Map<String, byte[]> classBytes) {
            this.classBytes = classBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
