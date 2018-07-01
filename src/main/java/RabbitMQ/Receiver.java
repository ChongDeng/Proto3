package RabbitMQ;

import RabbitMQ.Utils.ConnectionUtil;
import com.fqyang.AddressBookProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rabbitmq.client.*;

import java.io.IOException;

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

/**
 * Created by chong
 */
public class Receiver {
    private final static String QUEUE = "MQ_Protobuf";//队列的名字

    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE,false,false,false,null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        /* Since RabbitMQ Server will push messages asynchronously, we provide a callback
           in the form of an object that will buffer the messages until we're ready to use them
        * */
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                try {
                    AddressBookProtos.Person PersonCopy = AddressBookProtos.Person.parseFrom(body);
                    System.out.println("Received Message!");
                    System.out.println("Person ID: " + PersonCopy.getId());
                    System.out.println("Name: " + PersonCopy.getName());
                    System.out.println("Email: " + PersonCopy.getEmail());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        };
        //接收消息 ,参数2是自动确认
        channel.basicConsume(QUEUE, true, consumer);
    }
}
