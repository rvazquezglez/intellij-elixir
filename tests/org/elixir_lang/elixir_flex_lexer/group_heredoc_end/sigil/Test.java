package org.elixir_lang.elixir_flex_lexer.group_heredoc_end.sigil;

import com.intellij.psi.tree.IElementType;
import org.elixir_lang.elixir_flex_lexer.TokenTest;
import org.junit.Ignore;

import java.io.IOException;

/**
 * Created by luke.imhoff on 9/12/14.
 */
@Ignore("abstract")
public abstract class Test extends TokenTest {
    /*
     * Constructors
     */

    public Test(CharSequence charSequence, IElementType tokenType, int lexicalState, boolean consumeAll) {
        super(charSequence, tokenType, lexicalState, consumeAll);
    }

    /*
     * Methods
     */

    @Override
    protected void reset(CharSequence charSequence) throws IOException {
        CharSequence fullCharSequence = sigilName() + promoter() + '\n' + charSequence;
        super.reset(fullCharSequence);
        // consume sigilName
        flexLexer.advance();
        // consume promoter
        flexLexer.advance();
        // consume '\n'
        flexLexer.advance();
    }

    protected abstract String promoter();
    protected abstract char sigilName();
}
