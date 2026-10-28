package com.byteasy.common.component;

import java.util.ArrayList;
import java.util.List;

public class MutationContextHolder {

    private static final ThreadLocal<MutationContext> CONTEXT = ThreadLocal.withInitial(MutationContext::new);

    public static class MutationContext {
        private boolean mutated = false;
        private int totalAffectedRows = 0;
        private final List<String> mutatedStatements = new ArrayList<>();

        public boolean isMutated() {
            return mutated;
        }

        public int getTotalAffectedRows() {
            return totalAffectedRows;
        }

        public List<String> getMutatedStatements() {
            return mutatedStatements;
        }

        public void recordMutation(String statementId, int affectedRows) {
            // 只要执行了写操作且有影响行数（如果业务认为影响行数=0不算变更，可加 affectedRows > 0）
            if (affectedRows > 0) {
                this.mutated = true;
                this.totalAffectedRows += affectedRows;
                this.mutatedStatements.add(statementId);
            }
        }

        public void reset() {
            this.mutated = false;
            this.totalAffectedRows = 0;
            this.mutatedStatements.clear();
        }
    }

    public static MutationContext get() {
        return CONTEXT.get();
    }

    public static boolean hasMutation() {
        return CONTEXT.get().isMutated();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
