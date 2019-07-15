package warp.ast.decl.func;

import warp.ast.decl.Declaration;
import warp.ast.decl.param.ParameterDecl;
import warp.types.FunctionType;
import warp.types.Type;

import java.util.stream.Collectors;

public abstract class AbsMethodDecl extends Declaration {
    public String name;
    public boolean isOptional;
    protected Type returnType = new Type(Type.Kind.UNKNOWN);

    public FunctionType getType() {
        // todo - optimise this later
        return new FunctionType(children.stream()
                                        .filter((e)->e instanceof ParameterDecl)
                                        .map((e)->(ParameterDecl)e)
                                        .collect(Collectors.toList()),
                                returnType);
    }

}
