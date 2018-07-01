package RabbitMQ;

//
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易  
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//  

import RabbitMQ.Utils.ConnectionUtil;
import com.fqyang.AddressBookProtos;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Created by chong
 */
public class Sender {
    private final static String QUEUE = "MQ_Protobuf";//队列的名字

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();

        //声明队列,如果队列存在则什么都不做,如果不存在才创建
        // 参数1 队列的名字
        //参数2 是否持久化队列,我们的队列模式是在内存中的,如果 rabbitmq 重启会丢失,如果我们设置为 true, 则会保存到 erlang 自带的数据库中,重启后会重新读取
        //参数3 是否排外,有两个作用,第一个当我们的连接关闭后是否会自动删除队列,作用二 是否私有当天前队列,如果私有了,其他通道不可以访问当前队列,如果为 true, 一般是一个队列只适用于一个消费者的时候
        //参数4 是否自动删除
        //参数5 我们的一些其他参数
        channel.queueDeclare(QUEUE, false, false, false, null);

        AddressBookProtos.Person.Builder person =  AddressBookProtos.Person.newBuilder();

        person.setId(1);
        person.setName("Lilei");
        person.setEmail("fqyang@163.com");

        AddressBookProtos.Person.PhoneNumber.Builder MobileNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("4099134756");
        MobileNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
        person.addPhones(MobileNumber);

        AddressBookProtos.Person.PhoneNumber.Builder HomeNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("10086");
        HomeNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
        person.addPhones(HomeNumber);

        byte[] MessageEntity = person.build().toByteArray();

        //发送内容
         channel.basicPublish("",QUEUE,null, MessageEntity);
        //关闭连接
        channel.close();
        connection.close();

    }
}
