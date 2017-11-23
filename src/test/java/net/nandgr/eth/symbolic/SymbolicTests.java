package net.nandgr.eth.symbolic;

import net.nandgr.eth.bytecode.symexecution.DecisionsService;
import net.nandgr.eth.bytecode.symexecution.SymbolicPathsHandler;
import org.junit.Test;

public class SymbolicTests {

    @Test
    public void test_removeme() throws Exception {
        SymbolicPathsHandler symbolicPathsHandler = new SymbolicPathsHandler();
        DecisionsService.INSTANCE.subscribe(symbolicPathsHandler);

    }
}
