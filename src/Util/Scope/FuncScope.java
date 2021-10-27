package Util.Scope;

import Util.Entity.VarEntity;
import Util.Position;

import java.util.ArrayList;

public class FuncScope extends BaseScope {
    public ArrayList<VarEntity> paras = new ArrayList<VarEntity>();

    public FuncScope(BaseScope parentScope) {
        super(parentScope);
    }

    public void addPara(VarEntity para, Position pos) {
        paras.add(para);
        defineVar(para.name, para, pos);
    }
}
