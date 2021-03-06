package org.elixir_lang.annonator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import org.elixir_lang.ElixirSyntaxHighlighter;
import org.elixir_lang.psi.call.Call;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.elixir_lang.psi.call.name.Function.*;
import static org.elixir_lang.psi.call.name.Module.KERNEL;

/**
 * Annotates functions and macros from `Kernel` and `Kernel.SpecialForms` modules.
 */
public class Kernel implements Annotator, DumbAware {
    /*
     * CONSTANTS
     */

    private static final Set<String> RESOLVED_FUNCTION_NAME_SET = new HashSet<String>(
            Arrays.asList(
                    new String[] {
                            "abs",
                            "apply",
                            "apply",
                            "binary_part",
                            "bit_size",
                            "byte_size",
                            "div",
                            "elem",
                            "exit",
                            "function_exported?",
                            "get_and_update_in",
                            "get_in",
                            "hd",
                            "inspect",
                            "is_atom",
                            "is_binary",
                            "is_bitstring",
                            "is_boolean",
                            "is_float",
                            "is_function",
                            "is_function",
                            "is_integer",
                            "is_list",
                            "is_map",
                            "is_number",
                            "is_pid",
                            "is_port",
                            "is_reference",
                            "is_tuple",
                            "length",
                            "macro_exported?",
                            "make_ref",
                            "map_size",
                            "max",
                            "min",
                            "node",
                            "node",
                            "not",
                            "put_elem",
                            "put_in",
                            "rem",
                            "round",
                            "self",
                            "send",
                            "spawn",
                            "spawn",
                            "spawn_link",
                            "spawn_link",
                            "spawn_monitor",
                            "spawn_monitor",
                            "struct",
                            "throw",
                            "tl",
                            "trunc",
                            "tuple_size",
                            "update_in"
                    }
            )
    );

    private static final Set<String> RESOLVED_MACRO_NAME_SET = new HashSet<String>(
            Arrays.asList(
                    new String[] {
                            "@",
                            "alias!",
                            "and",
                            "binding",
                            DEF,
                            DEFDELEGATE,
                            DEFEXCEPTION,
                            DEFIMPL,
                            DEFMACRO,
                            DEFMACROP,
                            DEFMODULE,
                            DEFOVERRIDABLE,
                            DEFP,
                            DEFPROTOCOL,
                            DEFSTRUCT,
                            DESTRUCTURE,
                            "get_and_update_in",
                            IF,
                            "in",
                            "is_nil",
                            "match?",
                            "or",
                            "put_in",
                            "raise",
                            "raise",
                            "reraise",
                            "reraise",
                            "sigil_C",
                            "sigil_R",
                            "sigil_S",
                            "sigil_W",
                            "sigil_c",
                            "sigil_r",
                            "sigil_s",
                            "sigil_w",
                            "to_char_list",
                            "to_string",
                            UNLESS,
                            "update_in",
                            USE,
                            VAR_BANG
                    }
            )
    );

    private static final Set<String> RESOLVED_SPECIAL_FORMS_MACRO_NAME_SET = new HashSet<String>(
            Arrays.asList(
                    new String[]{
                            "__CALLER__",
                            "__DIR__",
                            "__ENV__",
                            __MODULE__,
                            "__aliases__",
                            "__block__",
                            ALIAS,
                            CASE,
                            COND,
                            "fn",
                            FOR,
                            IMPORT,
                            QUOTE,
                            RECEIVE,
                            REQUIRE,
                            "super",
                            "try",
                            UNQUOTE,
                            "unquote_splicing",
                            "with"
                    }
            )
    );

    /*
     * Public Instance Methods
     */

    /**
     * Annotates the specified PSI element.
     * It is guaranteed to be executed in non-reentrant fashion.
     * I.e there will be no call of this method for this instance before previous call get completed.
     * Multiple instances of the annotator might exist simultaneously, though.
     *
     * @param element to annotate.
     * @param holder  the container which receives annotations created by the plugin.
     */
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
        element.accept(
            new PsiRecursiveElementVisitor() {
                /*
                 * Public Instance Methods
                 */

                @Override
                public void visitElement(PsiElement element) {
                    if (element instanceof Call) {
                        visitCall((Call) element);
                    }
                }

                /*
                 * Private Instance Methods
                 */

                private void visitCall(Call call) {
                    String resolvedModuleName = call.resolvedModuleName();

                    if (resolvedModuleName != null && resolvedModuleName.equals(KERNEL)) {
                        PsiElement functionNameElement = call.functionNameElement();

                        if (functionNameElement != null) {
                            String resolvedFunctionName = call.resolvedFunctionName();

                            // a function can't take a `do` block
                            if (call.getDoBlock() == null) {
                                if (RESOLVED_FUNCTION_NAME_SET.contains(resolvedFunctionName)) {
                                    highlight(
                                            functionNameElement,
                                            holder,
                                            ElixirSyntaxHighlighter.FUNCTION_CALL,
                                            ElixirSyntaxHighlighter.PREDEFINED_CALL
                                    );
                                }
                            }

                            if (RESOLVED_MACRO_NAME_SET.contains(resolvedFunctionName) ||
                                    RESOLVED_SPECIAL_FORMS_MACRO_NAME_SET.contains(resolvedFunctionName)) {
                                highlight(
                                        functionNameElement,
                                        holder,
                                        ElixirSyntaxHighlighter.MACRO_CALL,
                                        ElixirSyntaxHighlighter.PREDEFINED_CALL
                                );
                            }
                        }
                    }
                }
            }
        );
    }

    /*
     * Private Instance Methods
     */

    /**
     * Highlights `element` with the given `textAttributesKey`.
     *
     * @param element element to highlight
     * @param annotationHolder the container which receives annotations created by the plugin.
     * @param textAttributesKeys text attributes to apply to the `element`.
     */
    private void highlight(@NotNull final PsiElement element, @NotNull AnnotationHolder annotationHolder, @NotNull final TextAttributesKey... textAttributesKeys) {
        annotationHolder
                .createInfoAnnotation(element, null)
                .setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);

        for (TextAttributesKey textAttributesKey : textAttributesKeys) {
            annotationHolder
                    .createInfoAnnotation(element, null)
                    .setEnforcedTextAttributes(
                            EditorColorsManager
                                    .getInstance()
                                    .getGlobalScheme()
                                    .getAttributes(textAttributesKey)
                    );
        }
    }
}
