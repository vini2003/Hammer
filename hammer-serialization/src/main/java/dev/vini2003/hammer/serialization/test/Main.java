package dev.vini2003.hammer.serialization.test;

import com.google.gson.JsonObject;
import dev.vini2003.hammer.serialization.api.common.parser.BufParser;
import dev.vini2003.hammer.serialization.api.common.parser.JsonParser;
import dev.vini2003.hammer.serialization.api.common.parser.NbtParser;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import javax.naming.ldap.SortKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
	public record Salary(
			Type type,
			Currency currency,
			double amount
	) {
		public enum Type {
			MONTHLY,
			WEEKLY,
			DAILY;
			
			public static final Node<Type> NODE = Node.STRING.xmap(Type::valueOf, Type::name);
		}
		
		public enum Currency {
			USD,
			EUR,
			GBP;
			
			public static final Node<Currency> NODE = Node.STRING.xmap(Currency::valueOf, Currency::name);
		}
		
		public static final Node<Salary> NODE = Node.compound(
				Type.NODE.key("type").getter(Salary::type),
				Currency.NODE.key("currency").getter(Salary::currency),
				Node.DOUBLE.key("amount").getter(Salary::amount),
				Salary::new
		);
	}
	
	public record Job(
			Degree degree,
			Salary salary
	) {
		public enum Degree {
			BACHELORS,
			DOCTORS,
			MASTERS;
			
			public static final Node<Degree> NODE = Node.STRING.xmap(Degree::valueOf, Degree::name);
		}
		
		public static final Node<Job> NODE = Node.compound(
				Degree.NODE.key("degree").getter(Job::degree),
				Salary.NODE.key("salary").getter(Job::salary),
				Job::new
		);
	}
	
	public record Person(
			String name,
			int age,
			Job job
	) {
		public static final Node<Person> NODE = Node.compound(
				Node.STRING.key("name").getter(Person::name),
				Node.INTEGER.key("age").getter(Person::age),
				Job.NODE.key("job").getter(Person::job),
				Person::new
		);
	}
	
	public record Event(
			String name,
			String description,
			Date date
	) {
		public static final Node<Event> NODE = Node.compound(
				Node.STRING.key("name").getter(Event::name),
				Node.STRING.key("description").getter(Event::description),
				Node.LONG.key("date").xmap(Date::new, Date::getTime).getter(Event::date),
				Event::new
		);
	}
	
	public record Company(
			String name,
			Map<String, Person> employees,
			List<Event> events
	) {
		public static final Node<Company> NODE = Node.compound(
				Node.STRING.key("name").getter(Company::name),
				Node.map(Node.STRING, Person.NODE).key("employees").getter(Company::employees),
				Node.list(Event.NODE).key("events").getter(Company::events),
				Company::new
		);
	}
	
	public static void main(String[] args) {
		var company = new Company(
				"Innit",
				Map.of(
						"Vini", new Person("Vini", 23, new Job(Job.Degree.MASTERS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 1000))),
						"Vini2", new Person("Vini2", 23, new Job(Job.Degree.MASTERS, new Salary(Salary.Type.MONTHLY, Salary.Currency.USD, 1000)))
				),
				List.of(
						new Event("Event 1", "Event 1 description", new Date()),
						new Event("Event 2", "Event 2 description", new Date())
				)
		);
		
		var eventNbt = new NbtCompound();
		var eventJson = new JsonObject();
		var eventBuf = new PacketByteBuf(Unpooled.buffer());
		
		Company.NODE.serialize(NbtParser.INSTANCE, company, eventNbt);
		Company.NODE.serialize(JsonParser.INSTANCE, company, eventJson);
		Company.NODE.serialize(BufParser.INSTANCE, company, eventBuf);
		
		var newEventFromNbt = Company.NODE.deserialize(NbtParser.INSTANCE, eventNbt);
		var newEventFromJson = Company.NODE.deserialize(JsonParser.INSTANCE, eventJson);
		var newEventFromBuf = Company.NODE.deserialize(BufParser.INSTANCE, eventBuf);
		
		
		System.out.println("Test finished!");
	}
}
