# Language Guide 0.0.1
> [!IMPORTANT]
> **This is a full working example of the language**
> **This is meant to be a proof of concept.
> **This is to help show the flow of the language and is not meant to be a direct source of documentation>

# Primitive Types
- string
- char
- byte
- float (float)
- int (integer)
- dbl (double)
- bool

# Composite Types 
- RegExp
- Record (immutable hashmap)
- Map (hashmap)
- Array (statically sized)
- List (dynamic)
- Queue
- LinkedList
- BT (binary tree)
- BST (binary search tree)
- Graph
- Set
- Stack


```
@myFunc<string>(string, number)
function myFunc (name, age) {
        return "My name is %name and I am %age years old";
}
```

### Explanation of above
- we can see the @myFunc:
- this gives the insight into the return type surrounded by "<>" and the types of the parameters
- this function description per-se has to exist above the function it defines

