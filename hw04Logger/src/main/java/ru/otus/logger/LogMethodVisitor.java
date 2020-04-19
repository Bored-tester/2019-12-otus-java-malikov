package ru.otus.logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class LogMethodVisitor extends MethodVisitor {
    private boolean isAnnotationPresent = false;
    private String methodName;
    private String description;
    private boolean isStatic;

    public LogMethodVisitor(int api, MethodVisitor methodVisitor, String methodName, String description, int access) {
        super(api, methodVisitor);
        this.methodName = methodName;
        this.description = description;
        isStatic = (access > Opcodes.ACC_STATIC);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if ("Lru/otus/logger/Log;".equals(desc)) {
            isAnnotationPresent = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {
        if (isAnnotationPresent) {
            // create string builder
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System",
                    "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
            mv.visitInsn(Opcodes.DUP);
            // add everything to the string builder
            mv.visitLdcInsn("\nMethod \"" + methodName + "\" was called with arguments: ");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "java/lang/StringBuilder", "<init>",
                    "(Ljava/lang/String;)V", false);
            List<String> argumentsTypes = getArgumentTypes(description);
            int varIndex = getFirstArgumentIndex();
            for (int i = 0; i < argumentsTypes.size(); i++) {
                String argumentType = argumentsTypes.get(i);
                mv.visitLdcInsn("\nArgument: ");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                //load var value
                int loadVarValueCode = getMatchingLoadCode(argumentType);
                mv.visitVarInsn(loadVarValueCode, varIndex);

//              as Long and Double takes two spaces on a stack
                if ((loadVarValueCode == Opcodes.LLOAD) | (loadVarValueCode == Opcodes.DLOAD))
                    varIndex += 2;
                else varIndex++;
                if (isStandardClass(argumentType)) {
//              append var value
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/StringBuilder", "append",
                            String.format("(%s)Ljava/lang/StringBuilder;", argumentType), false);
                } else {
//              append after convertion to string
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/Object", "toString",
                            "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/StringBuilder", "append",
                            "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                }
            }

//          print
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
            super.visitCode();
        }
    }

    private static boolean isStandardClass(String className) {
        return ((!className.startsWith("L")) | (className.startsWith("Ljava/lang/")));
    }

    private int getFirstArgumentIndex() {
        return isStatic ? 0 : 1;
    }

    private static int getMatchingLoadCode(String argumentType) {
        if (argumentType.startsWith("L")) {
            return Opcodes.ALOAD;
        }
        switch (argumentType) {
            case "I":
            case "B":
            case "Z":
            case "S":
                return Opcodes.ILOAD;
            case "D":
                return Opcodes.DLOAD;
            case "F":
                return Opcodes.FLOAD;
            case "J":
                return Opcodes.LLOAD;
            default:
                throw new IllegalArgumentException("unsupported argument type: " + argumentType);

        }
    }

    private static List<String> getArgumentTypes(String methodDescription) {
        List<String> parsedArgumentTypes = new ArrayList<>();
        String argumentDescriptions = methodDescription.substring(
                methodDescription.indexOf("(") + 1,
                methodDescription.lastIndexOf(")")
        );
        while (!argumentDescriptions.isEmpty()) {
            char typeLetter = argumentDescriptions.charAt(0);
            switch (typeLetter) {
                case 'L':
                    int semicolonIndex = argumentDescriptions.indexOf(';');
                    String parsedArgumentType = argumentDescriptions.substring(0, (semicolonIndex + 1));
                    parsedArgumentTypes.add(parsedArgumentType);
                    argumentDescriptions = argumentDescriptions.substring(semicolonIndex + 1);
                    break;
//              byte, short and int all are converted to int in String builder
                case 'I':
                case 'B':
                case 'S':
                    parsedArgumentTypes.add("I");
                    argumentDescriptions = argumentDescriptions.substring(1);
                    break;
                default:
                    parsedArgumentTypes.add("" + typeLetter);
                    argumentDescriptions = argumentDescriptions.substring(1);
            }
        }
        return parsedArgumentTypes;
    }
}
