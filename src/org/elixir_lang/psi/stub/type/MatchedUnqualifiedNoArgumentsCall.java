package org.elixir_lang.psi.stub.type;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import org.elixir_lang.psi.ElixirMatchedUnqualifiedNoArgumentsCall;
import org.elixir_lang.psi.impl.ElixirMatchedUnqualifiedNoArgumentsCallImpl;
import org.elixir_lang.psi.stub.type.call.Stub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MatchedUnqualifiedNoArgumentsCall extends Stub<org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall, ElixirMatchedUnqualifiedNoArgumentsCall> {
    /*
     * Constructors
     */

    public MatchedUnqualifiedNoArgumentsCall(@NotNull String debugName) {
        super(debugName);
    }

    /*
     * Instance Methods
     */

    @Override
    public ElixirMatchedUnqualifiedNoArgumentsCall createPsi(@NotNull org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall stub) {
        return new ElixirMatchedUnqualifiedNoArgumentsCallImpl(stub, this);
    }

    @Override
    public org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall createStub(@NotNull ElixirMatchedUnqualifiedNoArgumentsCall psi, StubElement parentStub) {
        return new org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall(
                parentStub,
                this,
                psi.resolvedModuleName(),
                psi.resolvedFunctionName(),
                psi.resolvedFinalArity(),
                psi.hasDoBlockOrKeyword(),
                StringUtil.notNullize(psi.getName(), "?"),
                psi.canonicalNameSet()
        );
    }

    @NotNull
    @Override
    public org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new org.elixir_lang.psi.stub.MatchedUnqualifiedNoArgumentsCall(
                parentStub,
                this,
                dataStream.readName(),
                dataStream.readName(),
                dataStream.readInt(),
                dataStream.readBoolean(),
                dataStream.readName(),
                readNameSet(dataStream)
        );
    }
}
