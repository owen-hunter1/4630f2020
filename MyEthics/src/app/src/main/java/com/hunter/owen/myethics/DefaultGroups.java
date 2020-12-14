package com.hunter.owen.myethics;

public interface DefaultGroups {
    final String[] STRING_DEFAULT_CLIMATE_TAGS = {"sustainable", "renewable", "polluting", "high-carbon-emissions", "plastic"};
    final String[] STRING_DEFAULT_VEGAN_TAGS = {"dairy-free", "vegan", "animal-tested"};
    final String[] STRING_DEFAULT_WORKERS_RIGHTS_TAGS = {"union", "american-made", "child-labor", "fair-trade"};

    final boolean[] LIKED_DEFAULT_CLIMATE_TAGS = {true, true, false, false, false};
    final boolean[] LIKED_DEFAULT_VEGAN_TAGS = {true, true, false};
    final boolean[] LIKED_DEFAULT_WORKERS_RIGHTS_TAGS = {true, true, false, true};

    final int[] RATING_DEFAULT_CLIMATE_TAGS = {5,5,5,5,5};
    final int[] RATING_DEFAULT_VEGAN_TAGS = {5,5,5};
    final int[] RATING_DEFAULT_WORKERS_RIGHTS_TAGS = {5,5,5,5};

    final EthicTag[] DEFAULT_CLIMATE_TAGS = EthicTag.createEthicArrayFromArrays(LIKED_DEFAULT_CLIMATE_TAGS, RATING_DEFAULT_CLIMATE_TAGS, STRING_DEFAULT_CLIMATE_TAGS);
    final EthicTag[] DEFAULT_VEGAN_TAGS = EthicTag.createEthicArrayFromArrays(LIKED_DEFAULT_VEGAN_TAGS, RATING_DEFAULT_VEGAN_TAGS, STRING_DEFAULT_VEGAN_TAGS);
    final EthicTag[] DEFAULT_WORKERS_RIGHTS_TAGS = EthicTag.createEthicArrayFromArrays(LIKED_DEFAULT_WORKERS_RIGHTS_TAGS, RATING_DEFAULT_WORKERS_RIGHTS_TAGS, STRING_DEFAULT_WORKERS_RIGHTS_TAGS);

}
