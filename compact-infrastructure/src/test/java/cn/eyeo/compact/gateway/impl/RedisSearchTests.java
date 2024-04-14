package cn.eyeo.compact.gateway.impl;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis Search Tests
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2024/4/14
 */
@Slf4j
public class RedisSearchTests {

    private static final String SEARCH_INDEX_NAME = "tf_film_info_index";


    @Test
    public void dropIndex() {
        try (RedisModulesClient client = RedisModulesClient.create("redis://localhost:6379")) {
            StatefulRedisModulesConnection<String, String> connect = client.connect();
            RedisModulesCommands<String, String> commands = connect.sync();

            String result = commands.ftDropindex(SEARCH_INDEX_NAME);
            log.info("ftDropIndex! index={}, result={}", SEARCH_INDEX_NAME, result);
        }
    }

    @Test
    public void createIndex() {
        // try with resource
        try (RedisModulesClient client = RedisModulesClient.create("redis://localhost:6379")) {
            StatefulRedisModulesConnection<String, String> connect = client.connect();
            RedisModulesCommands<String, String> commands = connect.sync();

            CreateOptions<String, String> options = CreateOptions.<String, String>builder()
                    .prefix(String.format("%s:", "film_info")).build();

            Field<String> name = Field.text("name").build();
            Field<String> types = Field.text("types").build();
            Field<String> score = Field.numeric("score").sortable(true).build();
            Field<String> comments = Field.numeric("comments").sortable(true).build();
            Field<String> year = Field.numeric("year").sortable(true).build();
            Field<String> summary = Field.text("summary").build();

            String result = commands.ftCreate(SEARCH_INDEX_NAME, options,
                    name, types, score, comments, year, summary);

            log.info("ftCreate! index={}, result={}", SEARCH_INDEX_NAME, result);
        }
    }

    @Test
    public void indexInfo() {
        try (RedisModulesClient client = RedisModulesClient.create("redis://localhost:6379")) {
            StatefulRedisModulesConnection<String, String> connect = client.connect();
            RedisModulesCommands<String, String> commands = connect.sync();

            List<Object> objects = commands.ftInfo(SEARCH_INDEX_NAME);
            for (Object object : objects) {
                log.info("ftInfo! index={}, info: {}", SEARCH_INDEX_NAME, object);
            }
        }
    }

    @Test
    public void addDoc() {
        final String str = getFilmInfoStr();

        Map<String, Map<String, String>> filmInfoMaps = new HashMap<>();

        String[] films = str.split("\n");
        for (String filmStr : films) {
            String[] infoStrArr = filmStr.split("\t");

            Map<String, String> filmInfoMap = new HashMap<>();
            filmInfoMap.put("id", infoStrArr[0]);
            filmInfoMap.put("name", infoStrArr[1]);
            filmInfoMap.put("types", infoStrArr[2]);
            filmInfoMap.put("score", infoStrArr[3]);
            filmInfoMap.put("comments", infoStrArr[4]);
            filmInfoMap.put("year", infoStrArr[5]);
            filmInfoMap.put("summary", infoStrArr[6]);

            filmInfoMaps.put(infoStrArr[0], filmInfoMap);
        }

        // try with resource
        try (RedisModulesClient client = RedisModulesClient.create("redis://localhost:6379")) {
            StatefulRedisModulesConnection<String, String> connect = client.connect();
            RedisModulesCommands<String, String> commands = connect.sync();

            for (Map.Entry<String, Map<String, String>> entry : filmInfoMaps.entrySet()) {
                Long result = commands.hset("film_info:" + entry.getKey(), entry.getValue());
                log.info("add doc, id={}, info={}, result={}", entry.getKey(), entry.getValue(), result);
            }
        }
    }

    @Test
    public void search() {
        // try with resource
        try (RedisModulesClient client = RedisModulesClient.create("redis://localhost:6379")) {
            StatefulRedisModulesConnection<String, String> connect = client.connect();
            RedisModulesCommands<String, String> commands = connect.sync();

            String query = "@types:动画";
            SearchOptions<String, String> options = new SearchOptions<>();
            options.setSortBy(SearchOptions.SortBy.desc("score"));
            SearchResults<String, String> results = commands.ftSearch(SEARCH_INDEX_NAME, query, options);

            log.info("ftSearch query={}, result_count={}", query, results.getCount());
            for (Document<String, String> result : results) {
                log.info("id={}, title={}, type={}, other=[{}]",
                        result.get("id"), result.get("name"), result.get("types"), result);
            }
        }
    }


    private static String getFilmInfoStr() {
        return "1\t肖申克的救赎\t美国/犯罪/剧情/\t9.7\t3011137\t1994\t希望让人自由。\n" +
                "2\t霸王别姬\t中国大陆/中国香港/剧情/爱情/同性/\t9.6\t2225366\t1993\t风华绝代。\n" +
                "3\t阿甘正传\t美国/剧情/爱情/\t9.5\t2243452\t1994\t一部美国近现代史。\n" +
                "4\t泰坦尼克号\t美国/墨西哥/剧情/爱情/灾难\t9.5\t2281726\t1997\t失去的才是永恒的。 \n" +
                "5\t千与千寻\t日本/剧情/动画/奇幻/\t9.4\t2329634\t2001\t最好的宫崎骏，最好的久石让。 \n" +
                "6\t这个杀手不太冷\t法国/美国/剧情/动作/犯罪/\t9.4\t2372657\t1994\t怪蜀黍和小萝莉不得不说的故事。\n" +
                "7\t美丽人生\t意大利/剧情/喜剧/爱情/战争/\t9.5\t1373614\t1997\t最美的谎言。\n" +
                "8\t星际穿越\t美国/英国/加拿大/剧情/科幻/冒险/\t9.4\t1944733\t2014\t爱是一种力量，让我们超越时空感知它的存在。\n" +
                "9\t盗梦空间\t美国/英国/剧情/科幻/悬疑/冒险/\t9.4\t2146881\t2010\t诺兰给了我们一场无法盗取的梦。\n" +
                "10\t楚门的世界\t美国/剧情/科幻/\t9.4\t1800294\t1998\t如果再也不能见到你，祝你早安，午安，晚安。\n" +
                "11\t辛德勒的名单\t美国/剧情/历史/战争/\t9.5\t1160745\t1993\t拯救一个人，就是拯救整个世界。\n" +
                "12\t忠犬八公的故事\t美国/英国/剧情/\t9.4\t1441427\t2009\t永远都不能忘记你所爱的人。\n" +
                "13\t海上钢琴师\t意大利/剧情/音乐/\t9.3\t1736307\t1998\t每个人都要走一条自己坚定了的路，就算是粉身碎骨。 \n" +
                "14\t三傻大闹宝莱坞\t印度/剧情/喜剧/爱情/歌舞/\t9.2\t1925426\t2009\t英俊版憨豆，高情商版谢耳朵。\n" +
                "15\t放牛班的春天\t法国/瑞士/德国/剧情/音乐/\t9.3\t1362849\t2004\t天籁一般的童声，是最接近上帝的存在。 \n" +
                "16\t机器人总动员\t美国/科幻/动画/冒险/\t9.3\t1367010\t2008\t小瓦力，大人生。\n" +
                "17\t疯狂动物城\t美国/喜剧/动画/冒险/\t9.2\t2035726\t2016\t迪士尼给我们营造的乌托邦就是这样，永远善良勇敢，永远出乎意料。\n" +
                "18\t无间道\t中国香港/剧情/犯罪/惊悚/\t9.3\t1427678\t2002\t香港电影史上永不过时的杰作。\n" +
                "19\t控方证人\t美国/剧情/犯罪/悬疑/\t9.6\t607519\t1957\t比利·怀德满分作品。\n" +
                "20\t大话西游之大圣娶亲\t中国香港/中国大陆/喜剧/爱情/奇幻/古装/\t9.2\t1587741\t1995\t一生所爱。\n" +
                "21\t熔炉\t韩国/剧情/\t9.4\t965632\t2011\t我们一路奋战不是为了改变世界，而是为了不让世界改变我们。\n" +
                "22\t教父\t美国/剧情/犯罪/\t9.3\t1009900\t1972\t千万不要记恨你的对手，这样会让你失去理智。\n" +
                "23\t触不可及\t法国/剧情/喜剧/\t9.3\t1171679\t2011\t满满温情的高雅喜剧。\n" +
                "24\t当幸福来敲门\t美国/剧情/传记/家庭/\t9.2\t1572863\t2006\t平民励志片。 \n" +
                "25\t寻梦环游记\t美国/喜剧/动画/奇幻/音乐/\t9.1\t1767706\t2017\t死亡不是真的逝去，遗忘才是永恒的消亡。\n" +
                "26\t末代皇帝\t英国/意大利/中国大陆/法国/剧情/传记/历史/\t9.3\t930590\t1987\t“不要跟我比惨，我比你更惨”再适合这部电影不过了。\n" +
                "27\t龙猫\t日本/动画/奇幻/冒险/\t9.2\t1312796\t1988\t人人心中都有个龙猫，童年就永远不会消失。\n" +
                "28\t怦然心动\t美国/剧情/喜剧/爱情/\t9.1\t1900841\t2010\t真正的幸福是来自内心深处。\n" +
                "29\t活着\t中国大陆/中国香港/剧情/历史/家庭/\t9.3\t886155\t1994\t张艺谋最好的电影。\n" +
                "30\t哈利·波特与魔法石\t美国/英国/奇幻/冒险/\t9.2\t1254729\t2001\t童话世界的开端。";
    }

}
