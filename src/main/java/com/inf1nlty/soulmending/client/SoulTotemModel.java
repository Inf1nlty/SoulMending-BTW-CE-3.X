package com.inf1nlty.soulmending.client;

import btw.block.model.BlockModel;
import btw.util.PrimitiveAABBWithBenefits;

public class SoulTotemModel extends BlockModel {
    public SoulTotemModel() {
        super();

        // 0. from [3, 0, 3], to [13, 1, 13], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                3 / 16.0, 0 / 16.0, 3 / 16.0,
                13 / 16.0, 1 / 16.0, 13 / 16.0,
                2
        ));

        // 1. from [4, 1, 4], to [12, 2, 12], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                4 / 16.0, 1 / 16.0, 4 / 16.0,
                12 / 16.0, 2 / 16.0, 12 / 16.0,
                2
        ));

        // 2. from [5, 2, 5], to [11, 8, 11], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                5 / 16.0, 2 / 16.0, 5 / 16.0,
                11 / 16.0, 8 / 16.0, 11 / 16.0,
                2
        ));

        // 3. from [4, 8, 4], to [12, 16, 12], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                4 / 16.0, 8 / 16.0, 4 / 16.0,
                12 / 16.0, 16 / 16.0, 12 / 16.0,
                2
        ));

        // 4. from [7, 6, 12], to [9, 10, 14], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                7 / 16.0, 6 / 16.0, 12 / 16.0,
                9 / 16.0, 10 / 16.0, 14 / 16.0,
                2
        ));

        // 5. from [3, 4, 11], to [13, 6, 13], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                3 / 16.0, 4 / 16.0, 11 / 16.0,
                13 / 16.0, 6 / 16.0, 13 / 16.0,
                2
        ));

        // 6. from [3, 5, 10], to [5, 7, 12], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                3 / 16.0, 5 / 16.0, 10 / 16.0,
                5 / 16.0, 7 / 16.0, 12 / 16.0,
                2
        ));

        // 7. from [11, 5, 10], to [13, 7, 12], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                11 / 16.0, 5 / 16.0, 10 / 16.0,
                13 / 16.0, 7 / 16.0, 12 / 16.0,
                2
        ));

        // 8. from [3, 6, 9], to [13, 8, 11], color 2
        addPrimitive(new PrimitiveAABBWithBenefits(
                3 / 16.0, 6 / 16.0, 9 / 16.0,
                13 / 16.0, 8 / 16.0, 11 / 16.0,
                2
        ));

        // 9. from [7, 3, 4], to [9, 4, 5], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                7 / 16.0, 3 / 16.0, 4 / 16.0,
                9 / 16.0, 4 / 16.0, 5 / 16.0,
                1
        ));

        // 10. from [5, 4, 4], to [11, 5, 5], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                5 / 16.0, 4 / 16.0, 4 / 16.0,
                11 / 16.0, 5 / 16.0, 5 / 16.0,
                1
        ));

        // 11. from [3, 5, 4], to [13, 6, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                3 / 16.0, 5 / 16.0, 4 / 16.0,
                13 / 16.0, 6 / 16.0, 8 / 16.0,
                1
        ));

        // 12. from [2, 6, 4], to [6, 7, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                2 / 16.0, 6 / 16.0, 4 / 16.0,
                6 / 16.0, 7 / 16.0, 8 / 16.0,
                1
        ));

        // 13. from [10, 6, 4], to [14, 7, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                10 / 16.0, 6 / 16.0, 4 / 16.0,
                14 / 16.0, 7 / 16.0, 8 / 16.0,
                1
        ));

        // 14. from [12, 7, 4], to [15, 8, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                12 / 16.0, 7 / 16.0, 4 / 16.0,
                15 / 16.0, 8 / 16.0, 8 / 16.0,
                1
        ));

        // 15. from [1, 7, 4], to [4, 8, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                1 / 16.0, 7 / 16.0, 4 / 16.0,
                4 / 16.0, 8 / 16.0, 8 / 16.0,
                1
        ));

        // 16. from [14, 8, 4], to [16, 9, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                14 / 16.0, 8 / 16.0, 4 / 16.0,
                16 / 16.0, 9 / 16.0, 8 / 16.0,
                1
        ));

        // 17. from [0, 8, 4], to [2, 9, 8], color 1
        addPrimitive(new PrimitiveAABBWithBenefits(
                0 / 16.0, 8 / 16.0, 4 / 16.0,
                2 / 16.0, 9 / 16.0, 8 / 16.0,
                1
        ));
    }
}