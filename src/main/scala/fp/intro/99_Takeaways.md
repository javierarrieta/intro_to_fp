# Takeaways for non-functional languages

* Try to use immutable data when possible and be explicit when
  mutating something.
* Send data explicitly to functions, try to avoid state 
  whenever possible (instance or global state)
* Be explicit about side-effects, split the logic into pure 
  and side-effecting parts when possible.
* Use functions as parameters when it splits the logic better.
